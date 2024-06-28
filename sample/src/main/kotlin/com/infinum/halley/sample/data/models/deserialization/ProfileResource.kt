// CPD-OFF
package com.infinum.halley.sample.data.models.deserialization

import com.infinum.halley.core.annotations.HalEmbedded
import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ProfileResource(

    @HalLink
    @SerialName(value = "self")
    val self: Link? = null,

    @SerialName(value = "name")
    val name: String? = null,

    @SerialName(value = "timezone")
    val timezone: String? = null,

    @HalEmbedded
    @SerialName(value = "blocked")
    val blocked: BlockedResource? = null,

    @HalEmbedded
    @SerialName(value = "user")
    val user: UserResource? = null,

    @Transient
    val age: Int = 40,

    @SerialName(value = "favouriteMeals")
    val favouriteMeals: Map<String, String>? = null,

    @SerialName(value = "categorizedAnimals")
    val categorizedAnimals: Map<String, List<AnimalResource>>? = null

) : HalResource
// CPD-ON
