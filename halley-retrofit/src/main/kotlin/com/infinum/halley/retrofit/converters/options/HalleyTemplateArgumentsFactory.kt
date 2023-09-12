package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.retrofit.annotations.HalTemplateArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArguments
import com.infinum.halley.retrofit.cache.HalleyOptionsCache

internal class HalleyTemplateArgumentsFactory(
    private val tag: String
) : OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap> {

    /*
        Cases:
        imperativan key - vrati samo
        anotation key - populiraj

     */
    override operator fun invoke(annotations: Array<out Annotation>): Arguments.Template? =
        (annotations.find { it.annotationClass == HalTemplateArguments::class } as? HalTemplateArguments)?.let {
            if (it.key.isBlank() && it.arguments.isNotEmpty()) {
                Arguments.Template(annotationParameters(it.arguments))
            } else if (it.key.isNotBlank() && it.arguments.isEmpty()) {
                Arguments.Template(cacheParameters(it.key))
            } else if (it.key.isNotBlank() && it.arguments.isNotEmpty()) {
                /**
                 * Keys from cache overwrite the keys from annotations
                 */
                Arguments.Template(
                    annotationParameters(it.arguments) + cacheParameters(it.key)
                )
            } else {
                HalleyOptionsCache.get(tag)?.template()
            }
        } ?: HalleyOptionsCache.get(tag)?.template()

    override fun annotationParameters(parameters: Array<HalTemplateArgument>): HalleyKeyedMap =
        parameters
            .associate { parameter ->
                parameter.name to parameter.entries
                    .associate { entry -> entry.key to entry.value }
                    .toMap()
            }

    override fun cacheParameters(key: String): HalleyKeyedMap =
        HalleyOptionsCache.get(tag)?.template()?.filterKeys { it == key } ?: mapOf()
}
