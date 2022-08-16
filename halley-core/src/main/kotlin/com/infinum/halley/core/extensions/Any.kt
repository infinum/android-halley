package com.infinum.halley.core.extensions

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

internal fun Any?.toJsonPrimitive() =
    when (this) {
        is Boolean? -> JsonPrimitive(this)
        is Number? -> JsonPrimitive(this)
        is String? -> JsonPrimitive(this)
        else -> JsonNull
    }
