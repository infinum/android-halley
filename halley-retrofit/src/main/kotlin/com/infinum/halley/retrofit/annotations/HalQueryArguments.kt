package com.infinum.halley.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
public annotation class HalQueryArguments(
    val key: String = "",
    val arguments: Array<HalQueryArgument> = []
)
