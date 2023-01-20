package com.infinum.halley.ktor

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.ktor.configuration.HalOptions
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.withCharsetIfNeeded
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.core.readText
import kotlin.reflect.KClass

/**
 * Creates a converter serializing with the specified [halley] Halley instance.
 */
public class HalleySerializationConverter(
    private val halley: Halley,
) : ContentConverter {

    private var options: Halley.Options? = null

    @Suppress("UNCHECKED_CAST")
    override suspend fun serializeNullable(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ): OutgoingContent {
        val content = halley.encodeToString(
            typeInfo.type as KClass<HalResource>,
            value as HalResource
        )
        return TextContent(content, contentType.withCharsetIfNeeded(charset))
    }

    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel
    ): Any {
        val contentPacket = content.readRemaining()
        return halley.decodeFromString(
            typeInfo.reifiedType,
            contentPacket.readText(charset = charset),
            options
        )
    }

    internal suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel,
        options: HalOptions
    ): Any {
        this.options = Halley.Options(
            common = options.common,
            query = options.query,
            template = options.template
        )
        val result = deserialize(charset, typeInfo, content)
        this.options = null
        return result
    }
}
