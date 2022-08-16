package com.infinum.halley.core.serializers.primitive.models

import java.lang.reflect.Type

internal data class PrimitiveHolder(
    val type: Type?,
    val name: String,
    val value: Any?
)
