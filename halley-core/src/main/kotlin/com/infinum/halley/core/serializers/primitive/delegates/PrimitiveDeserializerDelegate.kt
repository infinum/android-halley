package com.infinum.halley.core.serializers.primitive.delegates

import com.infinum.halley.core.extensions.extractClass
import com.infinum.halley.core.extensions.extractGenericClass
import com.infinum.halley.core.extensions.extractGenericType
import com.infinum.halley.core.extensions.extractType
import com.infinum.halley.core.extensions.isCollection
import com.infinum.halley.core.extensions.setField
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.shared.delegates.DeserializerDelegate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.serializer

internal class PrimitiveDeserializerDelegate<T : HalResource>(
    private val decoder: JsonDecoder,
    private val result: T,
    private val property: KProperty1<out HalResource, *>,
    private val jsonObject: JsonObject,
    private val name: String
) : DeserializerDelegate {

    override fun decode() {
        when (property.extractClass()) {
            Boolean::class -> property.setField(
                result,
                jsonObject[name]?.jsonPrimitive?.booleanOrNull
            )
            Int::class -> property.setField(result, jsonObject[name]?.jsonPrimitive?.intOrNull)
            Long::class -> property.setField(result, jsonObject[name]?.jsonPrimitive?.longOrNull)
            Double::class -> property.setField(
                result,
                jsonObject[name]?.jsonPrimitive?.doubleOrNull
            )
            Float::class -> property.setField(result, jsonObject[name]?.jsonPrimitive?.floatOrNull)
            String::class -> property.setField(
                result,
                jsonObject[name]?.jsonPrimitive?.contentOrNull
            )
            else -> if (property.isCollection()) {
                decodeCollection()
            } else {
                decodeObject()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun decodeCollection() {
        val childKlazz = property.extractGenericClass() as KClass<Any>
        jsonObject[name]?.jsonArray?.let {
            property.extractGenericType()?.let { type ->
                val klazz = property.extractClass()
                if (List::class.java.isAssignableFrom(klazz.java)) {
                    val value = decoder.json.decodeFromJsonElement(
                        ListSerializer(decoder.serializersModule.serializer(type)),
                        it
                    )
                    property.setField(result, value)
                } else if (Set::class.java.isAssignableFrom(klazz.java)) {
                    val value = decoder.json.decodeFromJsonElement(
                        SetSerializer(decoder.serializersModule.serializer(type)),
                        it
                    )
                    property.setField(result, value)
                } else if (Array::class.java.isAssignableFrom(klazz.java)) {
                    val value = decoder.json.decodeFromJsonElement(
                        ArraySerializer(childKlazz, decoder.serializersModule.serializer(type)),
                        it
                    )
                    property.setField(result, value)
                } else {
                    null
                }
            }
        }
    }

    override fun decodeObject() {
        val value: Any = decoder.json.decodeFromJsonElement(
            decoder.serializersModule.serializer(property.extractType()!!),
            jsonObject[name]?.let {
                when (it) {
                    is JsonPrimitive -> it.jsonPrimitive
                    is JsonObject -> it.jsonObject
                    else -> JsonNull
                }
            } ?: JsonNull
        )

        property.setField(result, value)
    }
}
