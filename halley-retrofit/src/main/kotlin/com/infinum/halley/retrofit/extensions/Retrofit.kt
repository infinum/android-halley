package com.infinum.halley.retrofit.extensions

import com.infinum.halley.core.Halley
import com.infinum.halley.core.extensions.halleyCallFactory
import com.infinum.halley.retrofit.converters.HalleyConverterFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Creates a new [Retrofit.Builder] which sets Retrofit's call factory and adds Halley as a converter factory.
 *
 * @param configuration Halley configuration.
 * @param callFactory The call factory to be used.
 * @return A new [Retrofit.Builder] with configured [callFactory]
 * and [HalleyConverterFactory] added as a converter factory with appropriate [configuration] and [callFactory].
 */
public fun Retrofit.Builder.withHalley(
    configuration: Halley.Configuration = Halley.Configuration(),
    callFactory: Call.Factory = OkHttpClient.Builder().build(),
): Retrofit.Builder = this.callFactory(callFactory.halleyCallFactory())
    .addConverterFactory(HalleyConverterFactory(configuration, callFactory))
