package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalQueryArguments
import com.infinum.halley.retrofit.cache.HalleyOptions
import com.infinum.halley.retrofit.cache.HalleyOptionsCache

internal class HalleyQueryArgumentsFactory(
    private val tag: String
) : OptionFactory<Arguments.Query?, HalQueryArgument, HalleyKeyedMap> {

    override operator fun invoke(annotations: Array<out Annotation>): Arguments.Query? =
        (annotations.find { it.annotationClass == HalQueryArguments::class } as? HalQueryArguments)?.let {
            if (it.key.isBlank() && it.arguments.isNotEmpty()) {
                Arguments.Query(annotationParameters(it.arguments))
            } else if (it.key.isNotBlank() && it.arguments.isEmpty()) {
                Arguments.Query(cacheParameters(it.key))
            } else if (it.key.isNotBlank() && it.arguments.isNotEmpty()) {
                /**
                 * Keys from cache overwrite the keys from annotations
                 */
                Arguments.Query(
                    annotationParameters(it.arguments) + cacheParameters(it.key)
                )
            } else {
                HalleyOptionsCache.get(tag)?.query()
            }
        } ?: HalleyOptionsCache.get(tag)?.query()

    override fun annotationParameters(parameters: Array<HalQueryArgument>): HalleyKeyedMap =
        parameters
            .associate { parameter ->
                parameter.name to parameter.entries
                    .associate { entry -> entry.key to entry.value }
                    .toMap()
            }

    override fun cacheParameters(key: String): HalleyKeyedMap =
        HalleyOptionsCache.get(tag)?.query()?.filterKeys { it == key } ?: mapOf()
}
