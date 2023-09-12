package com.infinum.halley.retrofit.converters

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.retrofit.converters.options.HalleyOptionsFactory
import java.lang.reflect.Type
import java.util.UUID
import okhttp3.ResponseBody
import retrofit2.Converter

internal class HalleyDeserializationConverter<T : HalResource>(
    private val halley: Halley,
    private val type: Type,
    private val options: Halley.Options?
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T =
        halley.decodeFromString(type, value.string(), options)
}
