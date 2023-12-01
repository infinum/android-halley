package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalTag
import com.infinum.halley.retrofit.annotations.HalTemplateArgument
import com.infinum.halley.retrofit.cache.HalleyOptions
import com.infinum.halley.retrofit.cache.HalleyOptionsCache

internal object HalleyOptionsFactory {

    private val commonFactory: OptionFactory<Arguments.Common?, HalArgumentEntry, HalleyMap> =
        HalleyCommonArgumentsFactory()
    private val queryFactory: OptionFactory<Arguments.Query?, HalQueryArgument, HalleyKeyedMap> =
        HalleyQueryArgumentsFactory()
    private val templateFactory: OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap> =
        HalleyTemplateArgumentsFactory()

    fun setAnnotations(annotations: Array<out Annotation>): String {
        val tag = (annotations.find { it.annotationClass == HalTag::class } as? HalTag)
            ?.value ?: throw NullPointerException("HalTag cannot be null or empty")

        val common: Arguments.Common? = commonFactory(tag, annotations)
        val query: Arguments.Query? = queryFactory(tag, annotations)
        val template: Arguments.Template? = templateFactory(tag, annotations)

        // populating the cache with the final values of the options
        if (common != null && query != null && template != null) {
            val options = HalleyOptions(
                common = common,
                query = query,
                template = template
            )
            HalleyOptionsCache.put(tag, options)
        }

        return tag
    }
}
