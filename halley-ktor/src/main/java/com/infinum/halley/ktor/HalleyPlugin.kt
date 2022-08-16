package com.infinum.halley.ktor

import com.infinum.halley.ktor.configuration.HalleyConfiguration
import com.infinum.halley.ktor.extensions.toHalOptions
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.accept
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.serialization.suitableCharset
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl
import io.ktor.utils.io.ByteReadChannel

public class HalleyPlugin internal constructor(
    private val configuration: HalleyConfiguration
) {

    @KtorDsl
    public companion object Plugin : HttpClientPlugin<HalleyConfiguration, HalleyPlugin> {

        private const val PLUGIN_KEY = "HalleyPlugin"

        public override val key: AttributeKey<HalleyPlugin> = AttributeKey(PLUGIN_KEY)

        override fun prepare(block: HalleyConfiguration.() -> Unit): HalleyPlugin =
            HalleyPlugin(
                HalleyConfiguration().apply(block)
            )

        @Suppress("ComplexMethod", "LongMethod")
        override fun install(plugin: HalleyPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload: Any ->
                val registrations = plugin.configuration.registrations()

                registrations.forEach { context.accept(it.contentTypeToSend) }

                if (subject is OutgoingContent) {
                    return@intercept
                }
                val contentType: ContentType = context.contentType() ?: return@intercept

                if (payload is Unit) {
                    context.headers.remove(HttpHeaders.ContentType)
                    proceedWith(EmptyContent)
                    return@intercept
                }

                val matchingRegistrations = registrations
                    .filter { it.contentTypeMatcher.contains(contentType) }
                    .takeIf { it.isNotEmpty() }
                    ?: return@intercept

                if (context.bodyType == null) {
                    return@intercept
                }

                context.headers.remove(HttpHeaders.ContentType)

                val serializedContent: OutgoingContent =
                    matchingRegistrations.firstNotNullOfOrNull { registration ->
                        registration.converter.serialize(
                            contentType,
                            contentType.charset() ?: Charsets.UTF_8,
                            context.bodyType!!,
                            payload
                        )
                    } ?: throw HalleyConverterException(
                        payload,
                        contentType,
                        matchingRegistrations.joinToString { it.converter.toString() }
                    )

                proceedWith(serializedContent)
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) {
                    return@intercept
                }
                if (info.type == ByteReadChannel::class) {
                    return@intercept
                }

                val contentType = context.response.contentType() ?: return@intercept

                val registrations = plugin.configuration.registrations()

                val matchingRegistrations = registrations
                    .filter { it.contentTypeMatcher.contains(contentType) }
                    .takeIf { it.isNotEmpty() }
                    ?: return@intercept

                val parsedBody = matchingRegistrations.firstNotNullOfOrNull { registration ->
                    registration
                        .converter
                        .let { it as? HalleySerializationConverter }
                        ?.deserialize(
                            context.request.headers.suitableCharset(),
                            info,
                            body,
                            context.attributes.toHalOptions()
                        )
                } ?: return@intercept

                proceedWith(
                    HttpResponseContainer(
                        info,
                        parsedBody
                    )
                )
            }
        }
    }
}
