package com.infinum.halley.ktor.extensions

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.ktor.configuration.HalOptions
import io.ktor.util.AttributeKey
import io.ktor.util.Attributes

internal fun Attributes.toHalOptions(): HalOptions =
    HalOptions(
        common = this.common,
        query = this.query,
        template = this.template
    )

private val Attributes.common: Arguments.Common?
    get() = this.getOrNull(AttributeKey(HalOptions.KEY_COMMON))

private val Attributes.query: Arguments.Query?
    get() = this.getOrNull(AttributeKey(HalOptions.KEY_QUERY))

private val Attributes.template: Arguments.Template?
    get() = this.getOrNull(AttributeKey(HalOptions.KEY_TEMPLATE))
