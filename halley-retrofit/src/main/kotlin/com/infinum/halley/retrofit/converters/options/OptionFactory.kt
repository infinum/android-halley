package com.infinum.halley.retrofit.converters.options

internal interface OptionFactory<Option, HalAnnotation, Values> {

    operator fun invoke(tag: String, annotations: Array<out Annotation>): Option

    fun annotationParameters(parameters: Array<HalAnnotation>): Values

    fun cacheParameters(tag: String, key: String): Values
}
