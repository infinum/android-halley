package com.infinum.halley.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target()
@MustBeDocumented
public annotation class HalArgumentEntry(
    val key: String,
    val value: String
)
