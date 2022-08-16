package com.infinum.halley.core.loader

import okhttp3.OkHttpClient

internal object HttpClientCache {

    private var httpClient: OkHttpClient = OkHttpClient.Builder().build()

    fun save(client: OkHttpClient) {
        httpClient = client
    }

    fun load(): OkHttpClient = httpClient
}
