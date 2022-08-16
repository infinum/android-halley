package com.infinum.halley.sample.data.models.deserialization

import com.infinum.halley.core.annotations.HalEmbedded
import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockedResource(

    @HalEmbedded
    @SerialName(value = "item")
    val item: List<OtherProfileResource>? = null

) : HalResource
