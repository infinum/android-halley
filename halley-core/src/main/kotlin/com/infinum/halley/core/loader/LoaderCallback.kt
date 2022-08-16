package com.infinum.halley.core.loader

import com.infinum.halley.core.serializers.embedded.models.relationship.RelationshipResponseHolder
import java.io.IOException
import java.util.concurrent.CountDownLatch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

internal class LoaderCallback(
    private val countDownLatch: CountDownLatch,
    private val onSuccess: (RelationshipResponseHolder) -> Unit
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        e.printStackTrace()
        countDownLatch.countDown()
    }

    override fun onResponse(call: Call, response: Response) {
        onSuccess(
            RelationshipResponseHolder(
                requestUrl = call.request().url.toString(),
                responseBody = response.body?.string()
            )
        )
        countDownLatch.countDown()
    }
}
