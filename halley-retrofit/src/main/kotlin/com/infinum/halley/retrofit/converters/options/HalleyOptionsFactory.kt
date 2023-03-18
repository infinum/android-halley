package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArgument

internal object HalleyOptionsFactory {

    private var annotations: Array<out Annotation> = emptyArray()

    private var options: Halley.Options? = null

    private val commonFactory: OptionFactory<Arguments.Common?, HalArgumentEntry, HalleyMap> =
        HalleyCommonArgumentsFactory()
    private val queryFactory: OptionFactory<Arguments.Query?, HalQueryArgument, HalleyKeyedMap> =
        HalleyQueryArgumentsFactory()
    private val templateFactory: OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap> =
        HalleyTemplateArgumentsFactory()

    fun setAnnotations(annotations: Array<out Annotation>) {
        this.annotations = annotations

        val common: Arguments.Common? = commonFactory(annotations)
        val query: Arguments.Query? = queryFactory(annotations)
        val templated: Arguments.Template? = templateFactory(annotations)

        this.options = Halley.Options(
            common = common,
            query = query,
            template = templated
        )
    }

    fun options(): Halley.Options? {
        val common: Arguments.Common? = commonFactory(this.annotations)
        val query: Arguments.Query? = queryFactory(this.annotations)
        val templated: Arguments.Template? = templateFactory(this.annotations)

        this.options = Halley.Options(
            common = common,
            query = query,
            template = templated
        )

        return this.options
    }
}
