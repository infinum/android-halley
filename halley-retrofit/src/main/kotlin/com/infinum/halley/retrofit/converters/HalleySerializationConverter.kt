package com.infinum.halley.retrofit.converters

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.hal.models.HalResource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

public class HalleySerializationConverter<T>(
    private val halley: Halley
) : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody =
        halley
            .encodeToString(value = value as HalResource)
            .toRequestBody("${Halley.CONTENT_TYPE}/${Halley.CONTENT_SUBTYPE}".toMediaType())
}
