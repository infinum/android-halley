package com.infinum.halley.retrofit.cache

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap
import com.infinum.halley.retrofit.converters.options.HalleyOptionsFactory

/*
This cannot be an object because integrators need to clear the arguments explitcity.
This needs to be constructed per call site. Convert to class.
 */
internal data class HalleyOptions(
    var common: Arguments.Common? = null,
    var query: Arguments.Query? = null,
    var template: Arguments.Template? = null

) {
    internal fun common(): Arguments.Common? = this.common

    internal fun query(): Arguments.Query? = this.query

    internal fun template(): Arguments.Template? = this.template
}

public fun halleyCommonOptions(tag: String, value: () -> HalleyMap) {
    HalleyOptionsFactory.addTag(tag)

    val exists = HalleyOptionsCache.check(tag)

    val newOption: HalleyOptions =
        if (exists) {
            val option = HalleyOptionsCache.get(tag)
            option?.copy(
                common = Arguments.Common(value.invoke())
            ) ?: HalleyOptions(
                common = Arguments.Common(value.invoke())
            )
        } else {
            HalleyOptions(
                common = Arguments.Common(value.invoke())
            )
        }

    HalleyOptionsCache.put(tag, newOption)
}

public fun halleyQueryOptions(tag: String, value: () -> HalleyKeyedMap) {
    HalleyOptionsFactory.addTag(tag)

    val exists = HalleyOptionsCache.check(tag)

    val newOption: HalleyOptions =
        if (exists) {
            val option = HalleyOptionsCache.get(tag)
            option?.copy(
                query = Arguments.Query(value.invoke())
            ) ?: HalleyOptions(
                query = Arguments.Query(value.invoke())
            )
        } else {
            HalleyOptions(
                query = Arguments.Query(value.invoke())
            )
        }

    HalleyOptionsCache.put(tag, newOption)
}

public fun halleyTemplateOptions(tag: String, value: () -> HalleyKeyedMap) {
    HalleyOptionsFactory.addTag(tag)

    val exists = HalleyOptionsCache.check(tag)

    val newOption: HalleyOptions =
        if (exists) {
            val option = HalleyOptionsCache.get(tag)
            option?.copy(
                template = Arguments.Template(value.invoke())
            ) ?: HalleyOptions(
                template = Arguments.Template(value.invoke())
            )
        } else {
            HalleyOptions(
                template = Arguments.Template(value.invoke())
            )
        }

    HalleyOptionsCache.put(tag, newOption)
}
