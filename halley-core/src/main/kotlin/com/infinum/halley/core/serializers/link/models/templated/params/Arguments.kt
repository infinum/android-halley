package com.infinum.halley.core.serializers.link.models.templated.params

import com.infinum.halley.core.typealiases.HalleyKeyedMap
import com.infinum.halley.core.typealiases.HalleyMap

/**
 * Params for templated links with values assigned dynamically in runtime.
 */
public sealed interface Arguments {

    public class Common(
        public val mappedValues: HalleyMap
    ) : Arguments, HalleyMap by mappedValues

    public class Query(
        private val mappedValues: HalleyKeyedMap
    ) : Arguments, HalleyKeyedMap by mappedValues

    public class Template(
        private val mappedValues: HalleyKeyedMap
    ) : Arguments, HalleyKeyedMap by mappedValues
}
