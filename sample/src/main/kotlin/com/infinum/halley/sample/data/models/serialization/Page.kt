package com.infinum.halley.sample.data.models.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(

    @SerialName(value = "size")
    val size: Int,

    @SerialName(value = "totalElements")
    val totalElements: Int,

    @SerialName(value = "totalPages")
    val totalPages: Int,

    @SerialName(value = "number")
    val number: Int
)
