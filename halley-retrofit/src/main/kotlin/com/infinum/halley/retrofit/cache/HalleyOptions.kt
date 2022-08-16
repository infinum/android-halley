package com.infinum.halley.retrofit.cache

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap

internal object HalleyOptions {

    internal var common: Arguments.Common? = null
    internal var query: Arguments.Query? = null
    internal var template: Arguments.Template? = null

    internal fun common(): Arguments.Common? = this.common

    internal fun query(): Arguments.Query? = this.query

    internal fun template(): Arguments.Template? = this.template
}

public fun halleyCommonOptions(value: () -> HalleyMap) {
    HalleyOptions.apply {
        this.common = Arguments.Common(value.invoke())
    }
}

public fun halleyQueryOptions(value: () -> HalleyKeyedMap) {
    HalleyOptions.apply {
        this.query = Arguments.Query(value.invoke())
    }
}

public fun halleyTemplateOptions(value: () -> HalleyKeyedMap) {
    HalleyOptions.apply {
        this.template = Arguments.Template(value.invoke())
    }
}
