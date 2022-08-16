package com.infinum.halley.sample.mock.client

import HAL
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.ktor.HalleyPlugin
import com.infinum.halley.sample.data.models.deserialization.ProfileResource
import defaultConfiguration
import halOptions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import okhttp3.OkHttpClient

class SampleKtor(
    private val httpClient: OkHttpClient
) {

    private val client = HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
            }
            port = 8080
            contentType(ContentType.HAL)
            halOptions(
                common = Arguments.Common(
                    mapOf(
                        "device" to "Motorola",
                        "rooted" to "false"
                    )
                ),
                query = Arguments.Query(
                    mapOf("animal" to mapOf("country" to "Germany"))
                ),
                template = Arguments.Template(
                    mapOf("animal" to mapOf("id" to "2"))
                )
            )
        }
        install(HalleyPlugin) {
            defaultConfiguration(
                encodeDefaults = true,
                ignoreUnknownKeys = true,
                prettyPrint = true,
                prettyPrintIndent = "  ",
                explicitNulls = false,
                httpClient = httpClient
            )
        }
    }

    suspend fun profile(): ProfileResource =
        client.get {
            url {
                path("api", "Profile", "self")
            }
        }.body()

    suspend fun profileWithOptions(): ProfileResource =
        client.get {
            url {
                path("api", "Profile", "self")
            }
            halOptions(
                query = Arguments.Query(
                    mapOf("animal" to mapOf("country" to "Italy"))
                ),
                template = Arguments.Template(
                    mapOf("animal" to mapOf("id" to "1"))
                )
            )
        }.body()
}
