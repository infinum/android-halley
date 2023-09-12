package com.infinum.halley.retrofit.converters

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.retrofit.converters.options.HalleyOptionsFactory
import java.lang.reflect.Type
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

internal class HalleyConverterFactory(
    configuration: Halley.Configuration,
    httpClient: OkHttpClient
) : Converter.Factory() {

    private val halley = Halley(configuration, httpClient)

    @Suppress("RedundantNullableReturnType") // Retaining interface contract.
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val options = HalleyOptionsFactory.options(annotations)
        return HalleyDeserializationConverter<HalResource>(
            halley,
            type,
            options
        )
    }

    @Suppress("RedundantNullableReturnType") // Retaining interface contract.
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? =
        HalleySerializationConverter<HalResource>(halley)
}
