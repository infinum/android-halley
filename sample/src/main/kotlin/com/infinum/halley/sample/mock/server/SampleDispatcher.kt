package com.infinum.halley.sample.mock.server

import com.infinum.halley.sample.mock.AssetLoader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SampleDispatcher : Dispatcher() {

    companion object {
        private const val OK = 200
        private const val NOT_FOUND = 404
        private const val NOT_IMPLEMENTED = 501
    }

    override fun dispatch(request: RecordedRequest): MockResponse =
        request.requestUrl?.let { resolvePath(it.encodedPathSegments) } ?: createNotImplemented()

    private fun resolvePath(pathSegments: List<String>): MockResponse =
        when {
            pathSegments.contains("Profile") ->
                if (pathSegments.last().toString() == "block") {
                    createSuccessResponse("blocked.json")
                } else {
                    createSuccessResponse("profile.json")
                }
            pathSegments.contains("User") ->
                createSuccessResponse("user.json")
            pathSegments.contains("Animal") ->
                when {
                    pathSegments.last() == "1" -> createSuccessResponse("animal_stiv.json")
                    pathSegments.last() == "2" -> createSuccessResponse("animal_bolt.json")
                    pathSegments.last() == "3" -> createSuccessResponse("animal_freya.json")
                    else -> createNotFound()
                }

            else -> createNotFound()
        }

    private fun createSuccessResponse(fileName: String): MockResponse =
        MockResponse()
            .setBody(AssetLoader.loadFile(fileName))
            .setHeader("Content-Type", "application/vnd.hal+json")
            .setResponseCode(OK)

    private fun createNotFound(): MockResponse =
        MockResponse()
            .setHeader("Content-Type", "application/vnd.hal+json")
            .setResponseCode(NOT_FOUND)

    private fun createNotImplemented(): MockResponse =
        MockResponse()
            .setHeader("Content-Type", "application/vnd.hal+json")
            .setResponseCode(NOT_IMPLEMENTED)
}
