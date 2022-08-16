package com.infinum.halley.sample.mock

object AssetLoader {

    fun loadFile(filename: String): String =
        javaClass.classLoader?.getResourceAsStream(filename)
            ?.reader()
            ?.readText()
            .orEmpty()
}
