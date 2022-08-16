package com.infinum.halley.sample.data.models.deserialization

import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimalResource(

    @HalLink
    @SerialName(value = "self")
    val self: Link? = null,

    @SerialName(value = "name")
    val name: String? = null,

    @SerialName(value = "age")
    val age: Int? = null

) : HalResource
