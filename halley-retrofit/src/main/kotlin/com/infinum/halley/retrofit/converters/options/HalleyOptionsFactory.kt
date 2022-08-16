package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArgument

internal class HalleyOptionsFactory(
    private val commonFactory: OptionFactory<Arguments.Common?, HalArgumentEntry, HalleyMap>,
    private val queryFactory: OptionFactory<Arguments.Query?, HalQueryArgument, HalleyKeyedMap>,
    private val templateFactory: OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap>
) : OptionFactory<Halley.Options, Unit, Unit> {

    override operator fun invoke(annotations: Array<out Annotation>): Halley.Options {
        val common: Arguments.Common? = commonFactory(annotations)

        val query: Arguments.Query? = queryFactory(annotations)

        val templated: Arguments.Template? = templateFactory(annotations)

        return Halley.Options(
            common = common,
            query = query,
            template = templated
        )
    }

    override fun annotationParameters(parameters: Array<Unit>) = Unit

    override fun cacheParameters(key: String) = Unit
}
