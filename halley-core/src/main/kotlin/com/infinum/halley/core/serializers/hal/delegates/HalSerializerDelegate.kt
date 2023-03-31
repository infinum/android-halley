package com.infinum.halley.core.serializers.hal.delegates

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.extractClass
import com.infinum.halley.core.extensions.extractCollection
import com.infinum.halley.core.extensions.extractGenericClass
import com.infinum.halley.core.extensions.extractGenericType
import com.infinum.halley.core.extensions.extractName
import com.infinum.halley.core.extensions.extractType
import com.infinum.halley.core.extensions.extractValue
import com.infinum.halley.core.extensions.isArray
import com.infinum.halley.core.extensions.isCollection
import com.infinum.halley.core.extensions.isHalEmbedded
import com.infinum.halley.core.extensions.isHalLink
import com.infinum.halley.core.extensions.isSet
import com.infinum.halley.core.extensions.isTransient
import com.infinum.halley.core.extensions.toJsonPrimitive
import com.infinum.halley.core.serializers.embedded.models.EmbeddedHolder
import com.infinum.halley.core.serializers.hal.HalSerializer
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import com.infinum.halley.core.serializers.link.models.LinkHolder
import com.infinum.halley.core.serializers.primitive.models.PrimitiveHolder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST") // We trust checking it manually.
internal class HalSerializerDelegate<T : HalResource>(
    private val encoder: JsonEncoder,
    private val value: T
) {

    fun encode(klazz: KClass<T>) {
        val primitives: MutableList<PrimitiveHolder> = mutableListOf()
        val links: MutableList<LinkHolder> = mutableListOf()
        val embedded: MutableList<EmbeddedHolder> = mutableListOf()

        klazz.memberProperties
            .toList()
            .filterNot { it.isTransient() }
            .forEach { property: KProperty1<out HalResource, *> ->
                when {
                    property.isHalLink() -> links.add(collectLink(property))
                    property.isHalEmbedded() -> embedded.add(collectEmbedded(property))
                    else -> primitives.add(collectPrimitive(property))
                }
            }

        encoder.encodeJsonElement(
            buildJsonObject {
                buildPrimitives(this, primitives)
                buildLinks(this, links)
                buildEmbedded(this, embedded)
            }
        )
    }

    private fun collectLink(property: KProperty1<out HalResource, *>): LinkHolder =
        LinkHolder(
            name = property.extractName(),
            value = if (property.isCollection()) {
                when {
                    property.isArray() -> property.extractValue(value) as Array<Link>?
                    property.isSet() -> property.extractValue(value) as Set<Link>?
                    else -> property.extractValue(value) as List<Link>?
                }
            } else {
                property.extractValue(value) as Link?
            }
        )

    private fun collectEmbedded(property: KProperty1<out HalResource, *>): EmbeddedHolder =
        EmbeddedHolder(
            name = property.extractName(),
            klazz = if (property.isCollection()) {
                property.extractGenericClass() as KClass<HalResource>
            } else {
                property.extractClass() as KClass<HalResource>
            },
            value = if (property.isCollection()) {
                property.extractCollection(value)
            } else {
                property.extractValue(value)
            }
        )

    private fun collectPrimitive(property: KProperty1<out HalResource, *>): PrimitiveHolder =
        PrimitiveHolder(
            type = if (property.isCollection()) {
                property.extractGenericType()
            } else {
                property.extractType()
            },
            name = property.extractName(),
            value = if (property.isCollection()) {
                property.extractCollection(value)
            } else {
                property.extractValue(value)
            }
        )

    @Suppress("NestedBlockDepth") // Makes more semantic sense to keep it nested like this.
    private fun buildPrimitives(builder: JsonObjectBuilder, primitives: List<PrimitiveHolder>) {
        if (primitives.isNotEmpty()) {
            primitives.forEach { holder ->
                holder.value?.let {
                    when (it::class) {
                        Boolean::class, Number::class, String::class -> {
                            builder.put(holder.name, holder.value.toJsonPrimitive())
                        }
                        else -> {
                            holder.type?.let { type ->
                                when (holder.value) {
                                    is List<*> -> builder.put(
                                        holder.name,
                                        encoder.json.encodeToJsonElement(
                                            ListSerializer(encoder.serializersModule.serializer(type)),
                                            holder.value as List<Any>
                                        )
                                    )
                                    is Set<*> -> builder.put(
                                        holder.name,
                                        encoder.json.encodeToJsonElement(
                                            SetSerializer(encoder.serializersModule.serializer(type)),
                                            holder.value as Set<Any>
                                        )
                                    )
                                    is Array<*> -> builder.put(
                                        holder.name,
                                        encoder.json.encodeToJsonElement(
                                            ArraySerializer(
                                                encoder.serializersModule.serializer(
                                                    type
                                                )
                                            ),
                                            holder.value as Array<Any>
                                        )
                                    )
                                    else -> builder.put(
                                        holder.name,
                                        encoder.json.encodeToJsonElement(
                                            encoder.serializersModule.serializer(type),
                                            holder.value
                                        )
                                    )
                                }
                            } ?: JsonNull
                        }
                    }
                } ?: JsonNull
            }
        }
    }

    private fun buildLinks(builder: JsonObjectBuilder, links: List<LinkHolder>) {
        if (links.isNotEmpty()) {
            builder.putJsonObject(Halley.CONSTANTS.LINKS) {
                links.forEach { holder ->
                    holder.value?.let {
                        when (holder.value) {
                            is List<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    ListSerializer(encoder.serializersModule.serializer()),
                                    holder.value as List<Link>
                                )
                            )
                            is Set<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    SetSerializer(encoder.serializersModule.serializer()),
                                    holder.value as Set<Link>
                                )
                            )
                            is Array<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    ArraySerializer(encoder.serializersModule.serializer()),
                                    holder.value as Array<Link>
                                )
                            )
                            else -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    encoder.serializersModule.serializer(),
                                    holder.value as Link
                                )
                            )
                        }
                    } ?: JsonNull
                }
            }
        }
    }

    private fun buildEmbedded(builder: JsonObjectBuilder, embedded: List<EmbeddedHolder>) {
        if (embedded.isNotEmpty()) {
            builder.putJsonObject(Halley.CONSTANTS.EMBEDDED) {
                embedded.forEach { holder ->
                    holder.value?.let {
                        when (holder.value) {
                            is List<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    ListSerializer(HalSerializer(holder.klazz!!)),
                                    holder.value as List<T>
                                )
                            )
                            is Set<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    SetSerializer(HalSerializer(holder.klazz!!)),
                                    holder.value as Set<T>
                                )
                            )
                            is Array<*> -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    ArraySerializer(HalSerializer(holder.klazz!!)),
                                    holder.value as Array<HalResource>
                                )
                            )
                            else -> put(
                                holder.name,
                                encoder.json.encodeToJsonElement(
                                    HalSerializer(holder.klazz!!),
                                    holder.value as T
                                )
                            )
                        }
                    } ?: JsonNull
                }
            }
        }
    }
}
