package com.infinum.halley.core.serializers.embedded.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.extractGenericClass
import com.infinum.halley.core.extensions.setField
import com.infinum.halley.core.loader.HalRelationshipLoader
import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipRequest
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
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("UNCHECKED_CAST") // We trust checking it manually.
internal class EmbeddedFromLinksDeserializerDelegate<T : HalResource>(
    private val decoder: JsonDecoder,
    private val result: T,
    private val property: KProperty1<out HalResource, *>,
    private val name: String,
    private val klazz: KClass<HalResource>,
    private val options: Halley.Options?
) : NestedDeserializerDelegate {

    private val loader = HalRelationshipLoader()

    @Suppress("NestedBlockDepth")
    override fun decode(jsonArray: JsonArray) {
        jsonArray.mapNotNull { element ->
            element.jsonObject[Halley.CONSTANTS.LINK.HREF]?.jsonPrimitive?.contentOrNull?.let {
                RelationshipRequest.Single(
                    name = name,
                    url = it,
                    templated = element.jsonObject[Halley.CONSTANTS.LINK.TEMPLATED]?.jsonPrimitive?.booleanOrNull
                        ?: false,
                    options = options
                )
            }
        }.let {
            loader
                .load(
                    requests = RelationshipRequest.Multiple(
                        requests = it
                    )
                )
                .map { holder -> decoder.json.parseToJsonElement(holder.responseBody!!).jsonObject }
                .let { jsonObjects ->
                    property.extractGenericClass()?.let { childKlazz ->
                        val itemSerializer = HalSerializer(
                            childKlazz as KClass<T>,
                            options
                        )
// CPD-OFF
                        if (List::class.java.isAssignableFrom(klazz.java)) {
                            val value: List<T> =
                                decoder.json.decodeFromJsonElement(
                                    ListSerializer(itemSerializer),
                                    JsonArray(jsonObjects)
                                )
                            property.setField(result, value)
                        } else if (Set::class.java.isAssignableFrom(klazz.java)) {
                            val value: Set<T> =
                                decoder.json.decodeFromJsonElement(
                                    SetSerializer(itemSerializer),
                                    JsonArray(jsonObjects)
                                )
                            property.setField(result, value)
                        } else if (Array::class.java.isAssignableFrom(klazz.java)) {
                            val value: Array<T> = decoder.json.decodeFromJsonElement(
                                ArraySerializer(childKlazz, itemSerializer),
                                JsonArray(jsonObjects)
                            )
                            property.setField(result, value)
                        } else {
                            property.setField(result, null)
                        }
// CPD-ON
                    }
                }
        }
    }

    override fun decode(jsonObject: JsonObject) {
        val value: HalResource? = jsonObject[name]?.jsonObject?.let { json ->
            json[Halley.CONSTANTS.LINK.HREF]
                ?.jsonPrimitive
                ?.contentOrNull
                ?.let { content ->
                    loader
                        .load(
                            request = RelationshipRequest.Single(
                                name = name,
                                url = content,
                                templated = json[Halley.CONSTANTS.LINK.TEMPLATED]
                                    ?.jsonPrimitive
                                    ?.booleanOrNull
                                    ?: false,
                                options = options
                            )
                        )
                        ?.responseBody
                }
        }?.let { json ->
            decoder.json.decodeFromString(
                HalSerializer(klazz, options),
                json
            )
        }

        property.setField(result, value)
    }
}
