package com.infinum.halley.sample.ui

object ResultCache {

    private var current: String = ""

    fun setCurrent(value: String) {
        current = value
    }

    fun current() = current

    fun clear() {
        current = ""
    }
}
