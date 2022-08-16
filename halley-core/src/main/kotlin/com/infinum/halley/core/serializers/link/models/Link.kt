package com.infinum.halley.core.serializers.link.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Link Object represents a hyperlink from the containing resource to a URI.
 */
@Serializable
public data class Link(

    /**
     * The "href" property is REQUIRED.
     * Its value is either a URI [RFC3986] or a URI Template [RFC6570].
     * If the value is a URI Template then the Link Object SHOULD have a
     * "templated" attribute whose value is true.
     */
    @SerialName(value = "href")
    val href: String,

    /**
     * The "templated" property is OPTIONAL.
     * Its value is boolean and SHOULD be true when the Link Object's
     * "href" property is a URI Template.
     * Its value SHOULD be considered false if it is undefined or any other value than true.
     */
    @SerialName(value = "templated")
    val templated: Boolean? = null,

    /**
     * The "type" property is OPTIONAL.
     * Its value is a String used as a hint to indicate
     * the media type expected when dereferencing the target resource.
     */
    @SerialName(value = "type")
    val type: String? = null,

    /**
     * The "deprecation" property is OPTIONAL.
     * Its presence indicates that the link is to be deprecated (i.e.removed)
     * at a future date.  Its value is a URL that SHOULD provide
     * further information about the deprecation.
     *
     * A client SHOULD provide some notification (for example, by logging a warning message)
     * whenever it traverses over a link that has this property.
     * The notification SHOULD include the deprecation property's value
     * so that a client manitainer can easily find information about the deprecation.
     */
    @SerialName(value = "deprecation")
    val deprecation: Boolean? = null,

    /**
     * The "name" property is OPTIONAL.
     * Its value MAY be used as a secondary key for selecting Link Objects
     * which share the same relation type.
     */
    @SerialName(value = "name")
    val name: String? = null,

    /**
     * The "profile" property is OPTIONAL.
     * Its value is a String which is a URI that hints about the profile
     * (as defined by [I-D.wilde-profile-link]) of the target resource.
     */
    @SerialName(value = "profile")
    val profile: String? = null,

    /**
     * The "title" property is OPTIONAL.
     * Its value is a string and is intended for labelling the link with a
     * human-readable identifier (as defined by [RFC5988]).
     */
    @SerialName(value = "title")
    val title: String? = null,

    /**
     * The "hreflang" property is OPTIONAL.
     * Its value is a string and is intended for indicating the language of
     * the target resource (as defined by [RFC5988]).
     */
    @SerialName(value = "hreflang")
    val hreflang: String? = null
)
