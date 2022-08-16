package com.infinum.halley.core.serializers.embedded.models

import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlin.reflect.KClass

internal data class EmbeddedHolder(
    val klazz: KClass<HalResource>?,
    val name: String,
    val value: Any?
)
