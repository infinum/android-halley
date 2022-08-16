import com.infinum.halley.core.Halley
import io.ktor.http.ContentType

public val ContentType.Companion.HAL: ContentType
    get() = ContentType(Halley.CONTENT_TYPE, Halley.CONTENT_SUBTYPE)

internal val ContentType.isHal: Boolean
    get() = this.withoutParameters().toString().let {
        it.startsWith("${Halley.CONTENT_TYPE}/") && it.endsWith(Halley.CONTENT_SUBTYPE)
    }
