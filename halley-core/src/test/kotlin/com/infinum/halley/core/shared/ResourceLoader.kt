package com.infinum.halley.core.shared

internal object ResourceLoader {

    operator fun invoke(filename: String): String =
        javaClass.classLoader?.getResourceAsStream(filename)
            ?.reader()
            ?.readText()
            .orEmpty()
}
