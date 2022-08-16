package com.infinum.halley.sample.data.models.serialization

import com.infinum.halley.core.annotations.HalEmbedded
import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResource(

    @HalLink
    @SerialName(value = "self")
    val self: Link? = null,

    @SerialName(value = "name")
    val name: String? = null,

    @SerialName(value = "weight")
    val weight: Int = 0,

    @SerialName(value = "description")
    val description: String? = null,

    @HalEmbedded
    @SerialName(value = "manufacturer")
    val manufacturer: ManufacturerResource? = null,

    @HalLink
    @SerialName(value = "other")
    val other: List<Link>? = null,

    @SerialName(value = "pages")
    val pages: List<Page>? = null,

    @SerialName(value = "hello")
    val hello: HelloWorld? = null

) : HalResource
