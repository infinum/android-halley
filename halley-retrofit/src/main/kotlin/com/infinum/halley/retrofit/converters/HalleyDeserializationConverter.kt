package com.infinum.halley.retrofit.converters

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.retrofit.cache.HalleyOptionsCache
import java.lang.reflect.Type
import okhttp3.ResponseBody
import retrofit2.Converter

internal class HalleyDeserializationConverter<T : HalResource>(
    private val halley: Halley,
    private val type: Type,
    private val tag: String
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T {
        return halley.decodeFromString(type, value.string(), getOptions())
    }

    private fun getOptions(): Halley.Options? =
        HalleyOptionsCache.get(tag)?.let {
            Halley.Options(
                common = it.common(),
                query = it.query(),
                template = it.template()
            )
        }
}
