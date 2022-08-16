package com.infinum.halley.core.serializers.embedded.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.extractClass
import com.infinum.halley.core.extensions.isCollection
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.shared.delegates.DeserializerDelegate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

@Suppress("UNCHECKED_CAST", "LongParameterList") // We trust checking it manually.
internal class EmbeddedDeserializerDelegate<T : HalResource>(
    private val decoder: JsonDecoder,
    private val result: T,
    private val property: KProperty1<out HalResource, *>,
    private val links: JsonObject?,
    private val embeddeds: JsonObject?,
    private val name: String,
    private val options: Halley.Options?
) : DeserializerDelegate {

    private val childKlazz: KClass<HalResource> = property.extractClass() as KClass<HalResource>

    override fun decode() {
        if (property.isCollection()) {
            decodeCollection()
        } else {
            decodeObject()
        }
    }

    override fun decodeCollection() {
        embeddeds?.get(name)?.jsonArray?.let {
            EmbeddedFromEmbeddedDeserializerDelegate(
                decoder,
                result,
                property,
                childKlazz,
                options
            ).decode(it)
        } ?: links?.get(name)?.jsonArray?.let {
            EmbeddedFromLinksDeserializerDelegate(
                decoder,
                result,
                property,
                name,
                childKlazz,
                options
            ).decode(it)
        }
    }

    override fun decodeObject() {
        embeddeds
            ?.get(name)
            ?.jsonObject?.let {
                EmbeddedFromEmbeddedDeserializerDelegate(
                    decoder,
                    result,
                    property,
                    childKlazz,
                    options
                ).decode(it)
            } ?: links?.let {
            EmbeddedFromLinksDeserializerDelegate(
                decoder,
                result,
                property,
                name,
                childKlazz,
                options
            ).decode(it)
        }
    }
}
