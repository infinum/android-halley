package com.infinum.halley.core.callfactories

import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response

internal class AsyncCallFactory(
    private val delegate: Call.Factory,
    private val executorService: ExecutorService,
) : Call.Factory {
    override fun newCall(request: Request): Call {
        return AsyncCall(delegate.newCall(request), executorService)
    }
}

internal class AsyncCall(
    private val delegate: Call,
    private val executorService: ExecutorService,
) : Call by delegate {

    private var task: Future<*>? = null

    override fun cancel() {
        delegate.cancel()
        task?.cancel(true)
    }

    override fun clone(): Call = AsyncCall(delegate.clone(), executorService)

    override fun enqueue(responseCallback: Callback) {
        delegate.enqueue(AsyncCallback(responseCallback))
    }

    internal inner class AsyncCallback(
        private val delegate: Callback,
    ) : Callback {
        override fun onFailure(call: Call, e: IOException) {
            task = executorService.submit { delegate.onFailure(call, e) }
        }

        override fun onResponse(call: Call, response: Response) {
            task = executorService.submit { delegate.onResponse(call, response) }
        }
    }
}
