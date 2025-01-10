package com.infinum.halley.core.loader

import com.damnhandy.uri.template.UriTemplate
import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipRequest
import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipResponseHolder
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyMap
import java.util.concurrent.CountDownLatch
import okhttp3.Request

internal class HalRelationshipLoader : RelationshipLoader {

    private val result: MutableList<RelationshipResponseHolder> = mutableListOf()

    override fun load(request: RelationshipRequest.Single): RelationshipResponseHolder? {
        replaceTemplatePlaceholders(request)?.let { url ->
            val countDownLatch = CountDownLatch(1)
            val callback = LoaderCallback(countDownLatch, result::add)

            val requestUrl = appendParameters(url, request.name, request.options)

            val call = CallFactoryCache.load().newCall(
                Request.Builder()
                    .url(requestUrl)
                    .build()
            )
            try {
                call.enqueue(callback)
                countDownLatch.await()
            } catch (interruptedException: InterruptedException) {
                call.cancel()
                throw interruptedException
            }
        }

        return result.firstOrNull()
    }

    /**
     * Loads other nested embedded objects per embedded object that it is currently deserialized.
     */
    override fun load(
        requests: RelationshipRequest.Multiple
    ): List<RelationshipResponseHolder> {
        val countDownLatch = CountDownLatch(requests.validParametersSize)
        val callback = LoaderCallback(countDownLatch, result::add)

        val calls = requests
            .requests
            .mapNotNull {
                replaceTemplatePlaceholders(it)?.let { url ->
                    val requestUrl = appendParameters(url, it.name, it.options)
                    Request.Builder()
                        .url(requestUrl)
                        .build()
                }
            }
            .map {
                CallFactoryCache.load().newCall(it)
            }

        try {
            calls.forEach { it.enqueue(callback) }
            countDownLatch.await()
        } catch (interruptedException: InterruptedException) {
            calls.forEach { it.cancel() }
            throw interruptedException
        }

        return result.toList()
    }

    private fun replaceTemplatePlaceholders(request: RelationshipRequest.Single): String? =
        if (request.templated) {
            request.options?.let { buildTemplate(request, it.common, it.template) }
        } else {
            request.url
        }

    /**
     * When adding multiple maps, last map keys overwrite
     * any previous from any previous map in the sum.
     */
    private fun buildTemplate(
        request: RelationshipRequest.Single,
        commonArguments: Arguments.Common?,
        templateArguments: Arguments.Template?
    ): String {
        val allArguments = commonArguments.orEmpty() + templateArguments.orEmpty()
        return allArguments[request.name]?.let { map ->
            @Suppress("UNCHECKED_CAST")
            buildTemplateWithParameters(request.url, map as HalleyMap)
        } ?: run {
            sanitizedTemplateUrlWithoutParameters(request.url)
        }
    }

    private fun buildTemplateWithParameters(url: String, arguments: HalleyMap): String =
        UriTemplate
            .buildFromTemplate(url)
            .build()
            .set(arguments)
            .expand()

    private fun sanitizedTemplateUrlWithoutParameters(url: String): String =
        UriTemplate
            .buildFromTemplate(url)
            .build()
            .expand()

    private fun appendParameters(
        url: String,
        name: String,
        parameters: Halley.Options?
    ): String {
        val uriBuilder = UriBuilder(url)

        parameters?.query?.get(name)?.forEach { entry ->
            uriBuilder.appendQueryParameter(entry.key, entry.value)
        }

        return uriBuilder.toString()
    }
}
