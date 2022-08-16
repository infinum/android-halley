package com.infinum.halley.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
public annotation class HalTemplateArguments(
    val key: String = "",
    val arguments: Array<HalTemplateArgument> = []
)
