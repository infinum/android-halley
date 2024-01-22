package com.infinum.halley.sample.mock.client

import com.infinum.halley.core.Halley
import com.infinum.halley.retrofit.cache.halleyCommonOptions
import com.infinum.halley.retrofit.extensions.asHalleyConverterFactory
import com.infinum.halley.sample.mock.client.services.SampleCoroutinesService
import com.infinum.halley.sample.mock.client.services.SampleRxService
import com.infinum.halley.sample.mock.client.services.SampleService
import java.util.UUID
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SampleClient(
    private val baseUrl: HttpUrl,
    private val httpClient: OkHttpClient
) {

    init {
        halleyCommonOptions(tag = UUID.randomUUID().toString()) {
            mapOf(
                "device" to "Nokia",
                "rooted" to "false"
            )
        }
    }

    val service: SampleService by lazy { retrofit().create(SampleService::class.java) }

    val serviceCoroutines: SampleCoroutinesService by lazy {
        retrofit().create(
            SampleCoroutinesService::class.java
        )
    }

    val serviceRx: SampleRxService by lazy { retrofit().create(SampleRxService::class.java) }

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(
            Json.asHalleyConverterFactory(
                configuration = Halley.Configuration(
                    prettyPrint = true,
                    prettyPrintIndent = "  "
                ),
                httpClient = httpClient
            )
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(httpClient)
        .baseUrl(baseUrl)
        .build()
}
