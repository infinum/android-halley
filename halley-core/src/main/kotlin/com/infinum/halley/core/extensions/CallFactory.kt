package com.infinum.halley.core.extensions

import com.infinum.halley.core.callfactories.AsyncCallFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import okhttp3.Call
import com.infinum.halley.core.loader.RelationshipLoader
import com.infinum.halley.core.callfactories.AsyncCall.AsyncCallback

/**
 * Creates a new [Call.Factory] that executes the callback in a new thread.
 *
 * By default, OkHttp executes the callback code on the dispatcher thread,
 * meaning that enqueuing another set of calls in the callback can exhaust the dispatcher (default maximum is 5).
 * To free the dispatcher thread as soon as possible,
 * [AsyncCallback] is used to switch the callback execution into another thread from a non-fixed-sized thread pool.
 * By doing so, [RelationshipLoader] blocks that thread instead of the dispatcher thread
 * while waiting for relationship calls to complete.
 *
 * @param executorService the executor service to use
 * @return a new [Call.Factory] that executes the callback in a new thread
 */
public fun Call.Factory.asyncCallFactory(
    executorService: ExecutorService = Executors.newCachedThreadPool(),
): Call.Factory = AsyncCallFactory(this, executorService)
