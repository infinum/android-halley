package com.infinum.halley.retrofit.converters

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import java.lang.reflect.Type
import okhttp3.ResponseBody
import retrofit2.Converter

internal class HalleyDeserializationConverter<T : HalResource>(
    private val halley: Halley,
    private val options: Halley.Options?,
    private val type: Type
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T =
        halley.decodeFromString(type, value.string(), options)
}
