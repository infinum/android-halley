package com.infinum.halley.core.loader

import com.infinum.halley.core.extensions.asyncCallFactory
import okhttp3.Call
import okhttp3.OkHttpClient

internal object CallFactoryCache {

    private var callFactory: Call.Factory = OkHttpClient.Builder().build().asyncCallFactory()

    fun save(callFactory: Call.Factory) {
        this.callFactory = callFactory
    }

    fun load(): Call.Factory = callFactory
}
