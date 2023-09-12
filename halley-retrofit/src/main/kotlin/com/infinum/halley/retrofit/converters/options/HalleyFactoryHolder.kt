package com.infinum.halley.retrofit.converters.options

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.annotations.HalArgumentEntry
import com.infinum.halley.retrofit.annotations.HalQueryArgument
import com.infinum.halley.retrofit.annotations.HalTemplateArgument

internal data class HalleyFactoryHolder(
    val commonFactory: OptionFactory<Arguments.Common?, HalArgumentEntry, HalleyMap>,
    val queryFactory: OptionFactory<Arguments.Query?, HalQueryArgument, HalleyKeyedMap>,
    val templateFactory: OptionFactory<Arguments.Template?, HalTemplateArgument, HalleyKeyedMap>
)
