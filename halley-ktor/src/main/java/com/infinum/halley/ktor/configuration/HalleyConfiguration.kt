package com.infinum.halley.ktor.configuration

import HAL
import io.ktor.http.ContentType
import io.ktor.http.ContentTypeMatcher
import io.ktor.serialization.Configuration
import io.ktor.serialization.ContentConverter

/**
 * A [HalleyPlugin] configuration that is used during installation.
 */
public class HalleyConfiguration : Configuration {

    internal class ConverterRegistration(
        val converter: ContentConverter,
        val contentTypeToSend: ContentType,
        val contentTypeMatcher: ContentTypeMatcher,
    )

    private val registrations = mutableListOf<ConverterRegistration>()

    /**
     * Registers a [contentType] to a specified [converter] with an optional [configuration] script for a converter.
     */
    public override fun <T : ContentConverter> register(
        contentType: ContentType,
        converter: T,
        configuration: T.() -> Unit
    ) {
        val matcher = when (contentType) {
            ContentType.HAL -> HalleyContentTypeMatcher
            else -> defaultMatcher(contentType)
        }
        registrations.add(
            ConverterRegistration(
                converter.apply(configuration),
                contentType,
                matcher
            )
        )
    }

    internal fun registrations() = registrations.toList()

    private fun defaultMatcher(pattern: ContentType): ContentTypeMatcher =
        object : ContentTypeMatcher {

            override fun contains(contentType: ContentType): Boolean =
                contentType.match(pattern)
        }
}
