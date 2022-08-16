package com.infinum.halley.core.shared.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface Primitive {

    @Serializable
    data class Model(

        @SerialName("age")
        val age: Int = 21,

        @SerialName("location")
        val location: String = "London",

        @SerialName("verified")
        val verified: Boolean = true
    ) : Primitive

    @Serializable
    data class ListModel(

        @SerialName("items")
        val items: List<Model> = listOf(
            Model(age = 21),
            Model(age = 22),
            Model(age = 23)
        )
    ) : Primitive

    @Serializable
    data class SetModel(

        @SerialName("items")
        val items: Set<Model> = setOf(
            Model(age = 21),
            Model(age = 22),
            Model(age = 23)
        )
    ) : Primitive

    @Serializable
    data class ArrayModel(

        @SerialName("items")
        val items: Array<Model> = arrayOf(
            Model(age = 21),
            Model(age = 22),
            Model(age = 23)
        )
    ) : Primitive {

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
    data class ListOfHal(

        @SerialName("items")
        val items: List<Hal.Primitive> = listOf(
            Hal.Primitive(age = 21),
            Hal.Primitive(age = 22),
            Hal.Primitive(age = 23)
        )
    ) : Primitive

    @Serializable
    data class SetOfHal(

        @SerialName("items")
        val items: Set<Hal.Primitive> = setOf(
            Hal.Primitive(age = 21),
            Hal.Primitive(age = 22),
            Hal.Primitive(age = 23)
        )
    ) : Primitive

    @Serializable
    data class ArrayOfHal(

        @SerialName("items")
        val items: Array<Hal.Primitive> = arrayOf(
            Hal.Primitive(age = 21),
            Hal.Primitive(age = 22),
            Hal.Primitive(age = 23)
        )
    ) : Primitive {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ArrayOfHal

            if (!items.contentEquals(other.items)) return false

            return true
        }

        override fun hashCode(): Int {
            return items.contentHashCode()
        }
    }
}
