package com.infinum.halley.ktor

import io.ktor.http.ContentType

public class HalleyConverterException(
    payload: Any,
    contentType: ContentType,
    matchingRegistrations: String
) : Exception(
    "Can't convert $payload with contentType $contentType using converters $matchingRegistrations"
)
