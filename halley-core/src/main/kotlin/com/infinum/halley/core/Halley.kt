package com.infinum.halley.core

import com.infinum.halley.core.extensions.asyncCallFactory
import com.infinum.halley.core.loader.CallFactoryCache
import com.infinum.halley.core.serializers.hal.HalSerializer
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.Call
import okhttp3.OkHttpClient

public class Halley(
    private val configuration: Configuration = Configuration(),
    callFactory: Call.Factory = OkHttpClient.Builder().build().asyncCallFactory(),
) {
    public companion object {
        public const val CONTENT_TYPE: String = "application"
        public const val CONTENT_SUBTYPE: String = "vnd.hal+json"

        internal object Constants {
            internal const val EMBEDDED = "_embedded"
            internal const val LINKS = "_links"

            internal object Link {
                internal const val HREF = "href"
                internal const val TEMPLATED = "templated"
                internal const val TYPE = "type"
                internal const val DEPRECATION = "deprecation"
                internal const val NAME = "name"
                internal const val PROFILE = "profile"
                internal const val TITLE = "title"
                internal const val HREFLANG = "hreflang"
            }

            internal val LINK = Link
            internal const val SELF = "self"
        }

        internal val CONSTANTS = Constants
    }

    private val format: Json = Json {
        encodeDefaults = this@Halley.configuration.encodeDefaults
        ignoreUnknownKeys = this@Halley.configuration.ignoreUnknownKeys
        isLenient = this@Halley.configuration.isLenient
        allowStructuredMapKeys = this@Halley.configuration.allowStructuredMapKeys
        prettyPrint = this@Halley.configuration.prettyPrint
        explicitNulls = this@Halley.configuration.explicitNulls
        prettyPrintIndent = this@Halley.configuration.prettyPrintIndent
        coerceInputValues = this@Halley.configuration.coerceInputValues
        useArrayPolymorphism = this@Halley.configuration.useArrayPolymorphism
        classDiscriminator = this@Halley.configuration.classDiscriminator
        allowSpecialFloatingPointValues = this@Halley.configuration.allowSpecialFloatingPointValues
        useAlternativeNames = this@Halley.configuration.useAlternativeNames
    }

    init {
        CallFactoryCache.save(callFactory)
    }

    public fun format(): Json = format

//    public inline fun <reified T : HalResource> encodeToString(value: T): String =
//        format().encodeToString(HalSerializer(T::class), value)

    @Suppress("UNCHECKED_CAST", "UNNECESSARY_NOT_NULL_ASSERTION")
    public inline fun <reified T> encodeToString(value: T): String =
        if (value is HalResource) {
            format().encodeToString(HalSerializer(value!!::class as KClass<HalResource>), value)
        } else {
            format().encodeToString(format().serializersModule.serializer(), value)
        }

    public fun <T : HalResource> encodeToString(klazz: KClass<T>, value: T): String =
        format().encodeToString(HalSerializer(klazz), value)

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T> decodeFromString(
        string: String,
        options: Options? = null
    ): T =
        if (HalResource::class.java.isAssignableFrom(T::class.java)) {
            format().decodeFromString(
                HalSerializer(T::class as KClass<HalResource>, options),
                string
            ) as T
        } else {
            format().decodeFromString(format().serializersModule.serializer(), string)
        }

    @Suppress("UNCHECKED_CAST")
    public fun <T : HalResource> decodeFromString(
        type: Type,
        string: String,
        options: Options? = null
    ): T =
        format().decodeFromString(HalSerializer((type as Class<T>).kotlin, options), string)

    @Suppress("LongParameterList") // Makes more semantic sense to keep long parameter list.
    public open class Configuration(
        public val encodeDefaults: Boolean = true, // Json.Default.configuration.encodeDefaults,
        public val ignoreUnknownKeys: Boolean = true, // Json.Default.configuration.ignoreUnknownKeys,
        public val isLenient: Boolean = Json.Default.configuration.isLenient,
        public val allowStructuredMapKeys: Boolean = Json.Default.configuration.allowStructuredMapKeys,
        public val prettyPrint: Boolean = Json.Default.configuration.prettyPrint,
        public val explicitNulls: Boolean = false, // Json.Default.configuration.explicitNulls,
        public val prettyPrintIndent: String = Json.Default.configuration.prettyPrintIndent,
        public val coerceInputValues: Boolean = Json.Default.configuration.coerceInputValues,
        public val useArrayPolymorphism: Boolean = Json.Default.configuration.useArrayPolymorphism,
        public val classDiscriminator: String = Json.Default.configuration.classDiscriminator,
        public val allowSpecialFloatingPointValues: Boolean =
            Json.Default.configuration.allowSpecialFloatingPointValues,
        public val useAlternativeNames: Boolean = Json.Default.configuration.useAlternativeNames
    )

    public open class Options(
        public val common: Arguments.Common? = null,
        public val query: Arguments.Query? = null,
        public val template: Arguments.Template? = null,
    )
}
