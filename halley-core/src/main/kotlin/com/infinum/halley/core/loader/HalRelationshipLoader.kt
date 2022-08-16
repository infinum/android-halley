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

            HttpClientCache.load().newCall(
                Request.Builder()
                    .url(requestUrl)
                    .build()
            ).enqueue(callback)

            countDownLatch.await()
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

        requests
            .requests
            .mapNotNull {
                replaceTemplatePlaceholders(it)?.let { url ->
                    val requestUrl = appendParameters(url, it.name, it.options)

                    Request.Builder()
                        .url(requestUrl)
                        .build()
                }
            }
            .forEach { HttpClientCache.load().newCall(it).enqueue(callback) }

        countDownLatch.await()

        return result.toList()
    }

    private fun replaceTemplatePlaceholders(request: RelationshipRequest.Single): String? =
        if (request.templated) {
            request.options?.let { buildTemplate(request, it.template) }
        } else {
            request.url
        }

    private fun buildTemplate(
        request: RelationshipRequest.Single,
        arguments: Arguments.Template?
    ) = arguments?.let { template ->
        template[request.name]?.let { map ->
            buildTemplateWithParameters(request.url, map)
        }
    }

    private fun buildTemplateWithParameters(url: String, arguments: HalleyMap): String =
        UriTemplate
            .buildFromTemplate(url)
            .build()
            .set(arguments)
            .expand()

    private fun appendParameters(
        url: String,
        name: String,
        parameters: Halley.Options?
    ): String {
        val uriBuilder = UriBuilder(url)

        parameters?.common?.forEach { entry ->
            uriBuilder.appendQueryParameter(entry.key, entry.value)
        }
        parameters?.query?.get(name)?.forEach { entry ->
            uriBuilder.appendQueryParameter(entry.key, entry.value)
        }

        return uriBuilder.toString()
    }
}
