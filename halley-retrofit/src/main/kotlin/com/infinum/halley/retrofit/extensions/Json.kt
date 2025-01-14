package com.infinum.halley.retrofit.extensions

import com.infinum.halley.core.Halley
import com.infinum.halley.retrofit.converters.HalleyConverterFactory
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Converter

@Deprecated(
    message = "Use Retrofit.Builder.withHalley(configuration, callFactory) instead.",
    replaceWith = ReplaceWith("Retrofit.Builder.withHalley(configuration, httpClient)"),
)
@JvmName("create")
public fun BinaryFormat.asHalleyConverterFactory(
    configuration: Halley.Configuration = Halley.Configuration(),
    httpClient: Call.Factory = OkHttpClient.Builder().build(),
): Converter.Factory =
    HalleyConverterFactory(
        configuration = configuration,
        callFactory = httpClient
    )

@Deprecated(
    message = "Use Retrofit.Builder.withHalley(configuration, callFactory) instead.",
    replaceWith = ReplaceWith("Retrofit.Builder.withHalley(configuration, callFactory)"),
)
@JvmName("create")
public fun Json.asHalleyConverterFactory(
    configuration: Halley.Configuration = Halley.Configuration(),
    httpClient: Call.Factory = OkHttpClient.Builder().build(),
): Converter.Factory =
    HalleyConverterFactory(
        configuration = configuration,
        callFactory = httpClient
    )
