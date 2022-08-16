package com.infinum.halley.retrofit.extensions

import com.infinum.halley.core.Halley
import com.infinum.halley.retrofit.converters.HalleyConverterFactory
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Converter

@JvmName("create")
public fun BinaryFormat.asHalleyConverterFactory(
    configuration: Halley.Configuration = Halley.Configuration(),
    httpClient: OkHttpClient = OkHttpClient.Builder().build()
): Converter.Factory =
    HalleyConverterFactory(
        configuration = configuration,
        httpClient = httpClient
    )

@JvmName("create")
public fun Json.asHalleyConverterFactory(
    configuration: Halley.Configuration = Halley.Configuration(),
    httpClient: OkHttpClient = OkHttpClient.Builder().build()
): Converter.Factory =
    HalleyConverterFactory(
        configuration = configuration,
        httpClient = httpClient
    )
