package com.infinum.halley.ktor.configuration

import HAL
import io.ktor.http.ContentType
import io.ktor.http.ContentTypeMatcher
import isHal

public object HalleyContentTypeMatcher : ContentTypeMatcher {

    override fun contains(contentType: ContentType): Boolean {
        if (contentType.match(ContentType.HAL)) {
            return true
        }

        return contentType.isHal
    }
}
