package com.infinum.halley.sample.data.models.serialization

import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HelloWorld(

    @SerialName(value = "name")
    val name: String
    
) : HalResource
