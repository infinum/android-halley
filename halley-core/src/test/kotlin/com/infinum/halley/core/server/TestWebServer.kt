package com.infinum.halley.core.server

import kotlin.concurrent.thread
import okhttp3.mockwebserver.MockWebServer

internal class TestWebServer {

    companion object {
        private const val PORT = 8008
    }

    private var mockWebServer: MockWebServer? = null

    fun start() {
        thread {
            mockWebServer?.shutdown()
            mockWebServer = null
            mockWebServer = MockWebServer().apply {
                dispatcher = TestResponseDispatcher()
            }
            mockWebServer?.start(PORT)
        }
    }

    fun stop() {
        thread {
            mockWebServer?.shutdown()
            mockWebServer = null
        }
    }
}
