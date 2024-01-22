package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalCommonArguments
import com.infinum.halley.retrofit.cache.HalleyOptionsCache

internal class HalleyCommonArgumentsFactory : OptionFactory<Arguments.Common?, HalArgumentEntry, HalleyMap> {

    override operator fun invoke(tag: String, annotations: Array<out Annotation>): Arguments.Common? =
        (annotations.find { it.annotationClass == HalCommonArguments::class } as? HalCommonArguments)?.let {
            if (it.arguments.isNotEmpty()) {
                /**
                 * Keys from cache overwrite the keys from annotations
                 */
                Arguments.Common(
                    annotationParameters(it.arguments) + cacheParameters(tag, "")
                )
            } else {
                cacheParameters(tag, "").takeIf { map -> map.isNotEmpty() }
                    ?.let { map -> Arguments.Common(map) }
            }
        } ?: cacheParameters(tag, "").takeIf { it.isNotEmpty() }?.let { Arguments.Common(it) }

    override fun annotationParameters(parameters: Array<HalArgumentEntry>): HalleyMap =
        parameters.associate { parameter ->
            parameter.key to parameter.value
        }

    override fun cacheParameters(tag: String, key: String): HalleyMap =
        HalleyOptionsCache.get(tag)?.common()?.mappedValues ?: mapOf()
}
