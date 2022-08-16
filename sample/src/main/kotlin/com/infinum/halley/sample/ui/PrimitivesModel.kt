package com.infinum.halley.sample.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PrimitivesModel(

    @SerialName("age")
    val age: Int = 21,

    @SerialName("location")
    val location: String = "London",

    @SerialName("verified")
    val verified: Boolean = true
)
