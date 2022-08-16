package com.infinum.halley.core.server

import com.infinum.halley.core.shared.ResourceLoader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

internal class TestResponseDispatcher : Dispatcher() {

    companion object {
        private const val OK = 200
        private const val NOT_FOUND = 404
        private const val NOT_IMPLEMENTED = 501

        private const val CONTENT_TYPE_KEY = "Content-Type"
        private const val CONTENT_TYPE_VALUE = "application/vnd.hal+json"
    }

    override fun dispatch(request: RecordedRequest): MockResponse =
        request.requestUrl
            ?.let { resolvePath(it.encodedPathSegments) }
            ?: createNotImplemented()

    private fun resolvePath(pathSegments: List<String>): MockResponse =
        when {
            pathSegments.contains("User") ->
                if (pathSegments.last().toString() == "1") {
                    createSuccessResponse("hal_user_1.json")
                } else if (pathSegments.last().toString() == "2") {
                    createSuccessResponse("hal_user_2.json")
                } else {
                    createNotFound()
                }
            else -> createNotFound()
        }

    private fun createSuccessResponse(fileName: String): MockResponse =
        MockResponse()
            .setBody(ResourceLoader(fileName))
            .setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
            .setResponseCode(OK)

    private fun createNotFound(): MockResponse =
        MockResponse()
            .setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
            .setResponseCode(NOT_FOUND)

    private fun createNotImplemented(): MockResponse =
        MockResponse()
            .setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
            .setResponseCode(NOT_IMPLEMENTED)
}
