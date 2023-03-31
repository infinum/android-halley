package com.infinum.halley.core.serializers.hal.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.extractName
import com.infinum.halley.core.extensions.isHalEmbedded
import com.infinum.halley.core.extensions.isHalLink
import com.infinum.halley.core.extensions.isTransient
import com.infinum.halley.core.serializers.embedded.delegates.EmbeddedDeserializerDelegate
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.delegates.LinkDeserializerDelegate
import com.infinum.halley.core.serializers.primitive.delegates.PrimitiveDeserializerDelegate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Suppress("UNCHECKED_CAST") // We trust checking it manually.
internal class HalDeserializerDelegate(
    private val decoder: JsonDecoder
) {

    fun <T : HalResource> decode(klazz: KClass<T>, options: Halley.Options?): T {
        val jsonObject = decoder.decodeJsonElement() as JsonObject
        val result: T = klazz.java.getDeclaredConstructor().newInstance() as T

        val links: JsonObject? = jsonObject[Halley.CONSTANTS.LINKS]?.jsonObject
        val embeddeds: JsonObject? = jsonObject[Halley.CONSTANTS.EMBEDDED]?.jsonObject

        result::class
            .memberProperties
            .toList()
            .filterNot { it.isTransient() }
            .forEach { property: KProperty1<out HalResource, *> ->
                val name = property.extractName()
                when {
                    property.isHalLink() -> LinkDeserializerDelegate(
                        result,
                        property,
                        links,
                        name
                    ).decode()
                    property.isHalEmbedded() -> EmbeddedDeserializerDelegate(
                        decoder,
                        result,
                        property,
                        links,
                        embeddeds,
                        name,
                        options
                    ).decode()
                    else -> PrimitiveDeserializerDelegate(
                        decoder,
                        result,
                        property,
                        jsonObject,
                        name
                    ).decode()
                }
            }
        return result
    }
}
