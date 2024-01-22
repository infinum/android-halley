package com.infinum.halley.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
public annotation class HalTag(
    val value: String
)
