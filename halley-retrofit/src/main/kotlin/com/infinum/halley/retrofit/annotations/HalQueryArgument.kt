package com.infinum.halley.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target()
@MustBeDocumented
public annotation class HalQueryArgument(
    val name: String = "",
    val entries: Array<HalArgumentEntry>
)
