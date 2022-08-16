package com.infinum.halley.core.serializers.embedded.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.extractGenericClass
import com.infinum.halley.core.extensions.setField
import com.infinum.halley.core.serializers.hal.HalSerializer
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.shared.delegates.NestedDeserializerDelegate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

@Suppress("UNCHECKED_CAST") // We trust checking it manually.
internal class EmbeddedFromEmbeddedDeserializerDelegate<T : HalResource>(
    private val decoder: JsonDecoder,
    private val result: T,
    private val property: KProperty1<out HalResource, *>,
    private val klazz: KClass<HalResource>,
    private val options: Halley.Options?
) : NestedDeserializerDelegate {

    override fun decode(jsonArray: JsonArray) {
        property.extractGenericClass()?.let { childKlazz ->
            val itemSerializer = HalSerializer(
                childKlazz as KClass<T>,
                options
            )
// CPD-OFF
            if (List::class.java.isAssignableFrom(klazz.java)) {
                val value: List<T> =
                    decoder.json.decodeFromJsonElement(ListSerializer(itemSerializer), jsonArray)
                property.setField(result, value)
            } else if (Set::class.java.isAssignableFrom(klazz.java)) {
                val value: Set<T> =
                    decoder.json.decodeFromJsonElement(SetSerializer(itemSerializer), jsonArray)
                property.setField(result, value)
            } else if (Array::class.java.isAssignableFrom(klazz.java)) {
                val value: Array<T> = decoder.json.decodeFromJsonElement(
                    ArraySerializer(childKlazz, itemSerializer),
                    jsonArray
                )
                property.setField(result, value)
            } else {
                property.setField(result, null)
            }
// CPD-ON
        }
    }

    override fun decode(jsonObject: JsonObject) {
        val value: Any = decoder.json.decodeFromJsonElement(
            HalSerializer(klazz, options),
            jsonObject
        )

        property.setField(result, value)
    }
}
