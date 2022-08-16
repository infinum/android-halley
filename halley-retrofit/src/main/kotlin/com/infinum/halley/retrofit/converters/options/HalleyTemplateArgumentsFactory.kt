package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.retrofit.annotations.HalTemplateArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArguments
import com.infinum.halley.retrofit.cache.HalleyOptions

internal class HalleyTemplateArgumentsFactory :
    OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap> {

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
                null
            }
        }

    override fun annotationParameters(parameters: Array<HalTemplateArgument>): HalleyKeyedMap =
        parameters
            .associate { parameter ->
                parameter.name to parameter.entries
                    .associate { entry -> entry.key to entry.value }
                    .toMap()
            }

    override fun cacheParameters(key: String): HalleyKeyedMap =
        HalleyOptions.template()?.filterKeys { it == key } ?: mapOf()
}
