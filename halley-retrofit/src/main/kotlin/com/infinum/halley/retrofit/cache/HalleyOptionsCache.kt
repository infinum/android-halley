package com.infinum.halley.retrofit.cache

internal object HalleyOptionsCache {

    private var cache = mapOf<String, HalleyOptions>()
    fun check(tag: String): Boolean =
        cache.containsKey(tag)

    fun get(tag: String): HalleyOptions? =
        cache[tag]

    fun put(tag: String, option: HalleyOptions) {
        cache = cache + mapOf(tag to option)
    }
}
