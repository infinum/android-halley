package com.infinum.halley.core.serializers.hal

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.delegates.HalDeserializerDelegate
import com.infinum.halley.core.serializers.hal.delegates.HalSerializerDelegate
import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

public class HalSerializer<T : HalResource>(
    private val klazz: KClass<T>,
    private val options: Halley.Options? = null
) : KSerializer<T> {

    override val descriptor: SerialDescriptor = serialDescriptor<HalResource>()

    override fun serialize(encoder: Encoder, value: T): Unit =
        HalSerializerDelegate(encoder as JsonEncoder, value).encode(klazz)

    override fun deserialize(decoder: Decoder): T =
        HalDeserializerDelegate(decoder as JsonDecoder).decode(klazz, options)
}
