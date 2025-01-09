package com.infinum.halley.retrofit.extensions

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.asyncCallFactory
import com.infinum.halley.retrofit.converters.HalleyConverterFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit

public fun Retrofit.Builder.withHalley(
    configuration: Halley.Configuration = Halley.Configuration(),
    callFactory: Call.Factory = OkHttpClient.Builder().build(),
): Retrofit.Builder = this.callFactory(callFactory.asyncCallFactory())
    .addConverterFactory(HalleyConverterFactory(configuration, callFactory))
