package com.infinum.halley.core.serializers.shared.delegates

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

internal interface NestedDeserializerDelegate {

    fun decode(jsonArray: JsonArray)

    fun decode(jsonObject: JsonObject)
}
