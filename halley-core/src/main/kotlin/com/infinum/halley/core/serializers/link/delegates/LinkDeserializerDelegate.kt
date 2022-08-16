package com.infinum.halley.core.serializers.link.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.isArray
import com.infinum.halley.core.extensions.isCollection
import com.infinum.halley.core.extensions.isList
import com.infinum.halley.core.extensions.isSet
import com.infinum.halley.core.extensions.setField
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import com.infinum.halley.core.serializers.shared.delegates.DeserializerDelegate
import kotlin.reflect.KProperty1
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class LinkDeserializerDelegate<T : HalResource>(
    private val result: T,
    private val property: KProperty1<out HalResource, *>,
    private val links: JsonObject?,
    private val name: String
) : DeserializerDelegate {

    override fun decode() {
        if (property.isCollection()) {
            decodeCollection()
        } else {
            decodeObject()
        }
    }

    override fun decodeCollection() {
        links?.get(name)?.jsonArray?.map { mapToLink(it.jsonObject) }.let {
            if (property.isSet()) {
                property.setField(result, it?.toSet())
            } else if (property.isArray()) {
                property.setField(result, it?.toTypedArray())
            } else if (property.isList()) {
                property.setField(result, it)
            } else {
                property.setField(result, null)
            }
        }
    }

    override fun decodeObject() {
        val value: Any? = links?.get(name)?.jsonObject?.let { mapToLink(it) }

        property.setField(result, value)
    }

    private fun mapToLink(jsonObject: JsonObject): Link =
        Link(
            href = jsonObject[Halley.CONSTANTS.LINK.HREF]?.jsonPrimitive?.content!!,
            templated = jsonObject[Halley.CONSTANTS.LINK.TEMPLATED]?.jsonPrimitive?.booleanOrNull,
            type = jsonObject[Halley.CONSTANTS.LINK.TYPE]?.jsonPrimitive?.contentOrNull,
            deprecation = jsonObject[Halley.CONSTANTS.LINK.DEPRECATION]?.jsonPrimitive?.booleanOrNull,
            name = jsonObject[Halley.CONSTANTS.LINK.NAME]?.jsonPrimitive?.contentOrNull,
            profile = jsonObject[Halley.CONSTANTS.LINK.PROFILE]?.jsonPrimitive?.contentOrNull,
            title = jsonObject[Halley.CONSTANTS.LINK.TITLE]?.jsonPrimitive?.contentOrNull,
            hreflang = jsonObject[Halley.CONSTANTS.LINK.HREFLANG]?.jsonPrimitive?.contentOrNull
        )
}
