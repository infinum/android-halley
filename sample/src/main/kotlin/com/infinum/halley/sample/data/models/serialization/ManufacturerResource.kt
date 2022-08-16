package com.infinum.halley.sample.data.models.serialization

import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManufacturerResource(

    @HalLink
    @SerialName(value = "self")
    val self: Link? = null,

    @HalLink
    @SerialName(value = "homepage")
    val homepage: Link? = null,

    @SerialName(value = "name")
    val name: String? = null

) : HalResource
