package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.retrofit.annotations.HalTag

internal object HalleyOptionsFactory {

    private var taggedFactories = mapOf<String, HalleyFactoryHolder>()

    fun addTag(tag: String) {
        taggedFactories = taggedFactories + mapOf(
            tag to HalleyFactoryHolder(
                HalleyCommonArgumentsFactory(tag),
                HalleyQueryArgumentsFactory(tag),
                HalleyTemplateArgumentsFactory(tag)
            )
        )
    }

    fun options(annotations: Array<out Annotation>): Halley.Options? {
        val tag = (annotations.find { it.annotationClass == HalTag::class } as? HalTag)
            ?.value ?: throw NullPointerException("HalTag cannot be null or empty")

        val taggedFactory = taggedFactories[tag]
            ?: throw NullPointerException(
                "Options are not tagged with same tag in annotations and in call site."
            )

        val common: Arguments.Common? = taggedFactory.commonFactory(annotations)
        val query: Arguments.Query? = taggedFactory.queryFactory(annotations)
        val templated: Arguments.Template? = taggedFactory.templateFactory(annotations)

        return Halley.Options(
            common = common,
            query = query,
            template = templated
        )
    }
}
