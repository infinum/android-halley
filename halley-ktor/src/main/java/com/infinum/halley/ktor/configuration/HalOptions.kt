package com.infinum.halley.ktor.configuration

import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import io.ktor.util.AttributeKey

public class HalOptions(
    internal val common: Arguments.Common? = null,
    internal val query: Arguments.Query? = null,
    internal val template: Arguments.Template? = null
) {

    public companion object {
        public const val KEY_COMMON: String = "halley_common"
        public const val KEY_QUERY: String = "halley_query"
        public const val KEY_TEMPLATE: String = "halley_template"
    }

    public val commonHalKey: AttributeKey<Arguments.Common> =
        AttributeKey(KEY_COMMON)

    public val queryHalKey: AttributeKey<Arguments.Query> =
        AttributeKey(KEY_QUERY)

    public val templateHalKey: AttributeKey<Arguments.Template> =
        AttributeKey(KEY_TEMPLATE)
}
