package com.infinum.halley.core.loader

import java.net.URI

/**
 * Wrapper for java.net.URI class modification.
 * Enables builder like modification of URI parts.
 *
 * @param link String - encoded URI link
 *
 * We created the class to move away from android.net.Uri.
 * This way we can create standard Java/Kotlin module without unnecessary overhead.
 * For the future use the Builder can be extended with additional method which will modify URI.
 * We didn't implement all part to make the implementation simple and lightweight.
 * If other means of modification will be necessary in the module do add the options inside the UriBuilder.
 */
internal class UriBuilder(
    link: String
) {

    private var uri: URI = URI.create(link)

    fun appendQueryParameter(key: String, value: String) =
        with(uri) {
            val parameter = "$key=$value"
            val queryPart = if (query.isNullOrBlank()) {
                parameter
            } else {
                "$query&$parameter"
            }
            uri = URI(scheme, userInfo, host, port, path, queryPart, fragment)
        }

    fun build(): URI = uri

    override fun toString(): String =
        uri.toString()
}
