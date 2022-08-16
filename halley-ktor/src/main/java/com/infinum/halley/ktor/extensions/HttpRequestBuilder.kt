// CPD-OFF
import com.infinum.halley.core.Halley
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.ktor.configuration.HalOptions
import io.ktor.client.request.HttpRequestBuilder

public fun HttpRequestBuilder.halOptions(
    options: Halley.Options? = null
) {
    this.halOptions(
        HalOptions(
            common = options?.common,
            query = options?.query,
            template = options?.template
        )
    )
}

public fun HttpRequestBuilder.halOptions(
    common: Arguments.Common? = null,
    query: Arguments.Query? = null,
    template: Arguments.Template? = null
) {
    this.halOptions(
        HalOptions(
            common = common,
            query = query,
            template = template
        )
    )
}

private fun HttpRequestBuilder.halOptions(options: HalOptions) {
    this.setAttributes {
        options.common?.let { put(options.commonHalKey, it) }
        options.query?.let { put(options.queryHalKey, it) }
        options.template?.let { put(options.templateHalKey, it) }
    }
}
// CPD-ON
