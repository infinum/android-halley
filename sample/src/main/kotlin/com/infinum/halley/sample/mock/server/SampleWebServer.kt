package com.infinum.halley.sample.mock.server

import com.infinum.halley.sample.mock.client.SampleClient
import com.infinum.halley.sample.mock.client.SampleKtor
import kotlin.concurrent.thread
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer

class SampleWebServer {

    companion object {
        private const val PORT = 8080
    }

    private var mockWebServer: MockWebServer? = null
    private var client: SampleClient? = null
    private var ktor: SampleKtor? = null

    private val callFactory: Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        .build()

    fun start() {
        thread {
            mockWebServer?.shutdown()
            mockWebServer = null
            mockWebServer = MockWebServer().apply {
                dispatcher = SampleDispatcher()
            }
            mockWebServer?.start(PORT)
            client = SampleClient(mockWebServer?.url("/api/")!!, callFactory)
            ktor = SampleKtor(callFactory)
        }
    }

    fun stop() {
        thread {
            mockWebServer?.shutdown()
            mockWebServer = null
        }
    }

    fun client() = client

    fun ktor() = ktor
}
