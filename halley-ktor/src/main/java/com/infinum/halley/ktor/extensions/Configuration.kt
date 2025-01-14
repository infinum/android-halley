import com.infinum.halley.core.Halley
import com.infinum.halley.ktor.HalleySerializationConverter
import io.ktor.http.ContentType
import io.ktor.serialization.Configuration
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient

/**
 * Registers the `application/vnd.hal+json` content type to the [HalleyNegotiation] plugin.
 *
 * The example below shows how to register the Halley serializer with
 * customized configuration settings and some HTTP client call factory instance:
 * ```kotlin
 *  install(HalleyPlugin) {
 *      defaultConfiguration(
 *          encodeDefaults = true,
 *          ignoreUnknownKeys = true,
 *          prettyPrint = true,
 *          prettyPrintIndent = "  ",
 *          explicitNulls = false,
 *          httpClient = httpClient,
 *      )
 *  }
 * ```
 */
@Suppress("LongParameterList") // Makes more semantic sense to keep long parameter list.
public fun Configuration.defaultConfiguration(
    encodeDefaults: Boolean = Json.Default.configuration.encodeDefaults,
    ignoreUnknownKeys: Boolean = Json.Default.configuration.ignoreUnknownKeys,
    isLenient: Boolean = Json.Default.configuration.isLenient,
    allowStructuredMapKeys: Boolean = Json.Default.configuration.allowStructuredMapKeys,
    prettyPrint: Boolean = Json.Default.configuration.prettyPrint,
    explicitNulls: Boolean = Json.Default.configuration.explicitNulls,
    prettyPrintIndent: String = Json.Default.configuration.prettyPrintIndent,
    coerceInputValues: Boolean = Json.Default.configuration.coerceInputValues,
    useArrayPolymorphism: Boolean = Json.Default.configuration.useArrayPolymorphism,
    classDiscriminator: String = Json.Default.configuration.classDiscriminator,
    allowSpecialFloatingPointValues: Boolean =
        Json.Default.configuration.allowSpecialFloatingPointValues,
    useAlternativeNames: Boolean = Json.Default.configuration.useAlternativeNames,
    httpClient: Call.Factory = OkHttpClient.Builder().build(),
) {
    register(
        ContentType.HAL,
        HalleySerializationConverter(
            Halley(
                configuration = Halley.Configuration(
                    encodeDefaults = encodeDefaults,
                    ignoreUnknownKeys = ignoreUnknownKeys,
                    isLenient = isLenient,
                    allowStructuredMapKeys = allowStructuredMapKeys,
                    prettyPrint = prettyPrint,
                    explicitNulls = explicitNulls,
                    prettyPrintIndent = prettyPrintIndent,
                    coerceInputValues = coerceInputValues,
                    useArrayPolymorphism = useArrayPolymorphism,
                    classDiscriminator = classDiscriminator,
                    allowSpecialFloatingPointValues = allowSpecialFloatingPointValues,
                    useAlternativeNames = useAlternativeNames,
                ),
                httpClient = httpClient,
            )
        )
    )
}
