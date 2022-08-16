package com.infinum.halley.core.shared.models

import com.infinum.halley.core.serializers.hal.models.HalResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface Empty {

    @Serializable
    object Model : Empty

    @Serializable
    data class ListModel(

        @SerialName("items")
        val items: List<Model> = listOf()
    ) : Empty

    @Serializable
    data class SetModel(

        @SerialName("items")
        val items: Set<Model> = setOf()
    ) : Empty

    @Serializable
    data class ArrayModel(

        @SerialName("items")
        val items: Array<Model> = arrayOf()
    ) : Empty {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ArrayModel

            if (!items.contentEquals(other.items)) return false

            return true
        }

        override fun hashCode(): Int {
            return items.contentHashCode()
        }
    }

    @Serializable
    class HalModel : HalResource, Empty

    @Serializable
    data class HalList(

        @SerialName("items")
        val items: List<HalModel> = listOf()

    ) : HalResource, Empty

    @Serializable
    data class HalSet(

        @SerialName("items")
        val items: Set<HalModel> = setOf()

    ) : HalResource, Empty

    @Serializable
    data class HalArray(

        @SerialName("items")
        val items: Array<HalModel> = arrayOf()

    ) : HalResource, Empty {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as HalArray

            if (!items.contentEquals(other.items)) return false

            return true
        }

        override fun hashCode(): Int {
            return items.contentHashCode()
        }
    }
}
