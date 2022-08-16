package com.infinum.halley.core.shared.models

import com.infinum.halley.core.annotations.HalEmbedded
import com.infinum.halley.core.annotations.HalLink
import com.infinum.halley.core.serializers.hal.models.HalResource
import com.infinum.halley.core.serializers.link.models.Link
import com.infinum.halley.core.shared.models.Primitive.Model as PrimitiveModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface Hal {

    @Serializable
    data class Primitive(

        @SerialName("age")
        val age: Int = 21,

        @SerialName("location")
        val location: String = "London",

        @SerialName("verified")
        val verified: Boolean = true

    ) : HalResource, Hal

    @Serializable
    data class Model(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        )

    ) : HalResource, Hal

    @Serializable
    data class OtherModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = null,

        @SerialName(value = "name")
        val name: String? = null

    ) : HalResource, Hal

    @Serializable
    data class ListOfPrimitives(

        @SerialName("items")
        val items: List<PrimitiveModel> = listOf(
            PrimitiveModel(age = 21),
            PrimitiveModel(age = 22),
            PrimitiveModel(age = 23)
        )
    ) : HalResource, Hal

    @Serializable
    data class SetOfPrimitives(

        @SerialName("items")
        val items: Set<PrimitiveModel> = setOf(
            PrimitiveModel(age = 21),
            PrimitiveModel(age = 22),
            PrimitiveModel(age = 23)
        )
    ) : HalResource, Hal

    @Serializable
    data class ArrayOfPrimitives(

        @SerialName("items")
        val items: Array<PrimitiveModel> = arrayOf(
            PrimitiveModel(age = 21),
            PrimitiveModel(age = 22),
            PrimitiveModel(age = 23)
        )
    ) : HalResource, Hal {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ArrayOfPrimitives

            if (!items.contentEquals(other.items)) return false

            return true
        }

        override fun hashCode(): Int {
            return items.contentHashCode()
        }
    }

    @Serializable
    data class WithPrimitives(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @SerialName(value = "name")
        val name: String = "Bolt"

    ) : HalResource, Hal

    @Serializable
    data class WithPrimitivesList(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @SerialName(value = "names")
        val names: List<String> = listOf("Bolt", "Stiv")

    ) : HalResource, Hal

    @Serializable
    data class WithPrimitivesSet(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @SerialName(value = "names")
        val names: Set<String> = setOf("Bolt", "Stiv")

    ) : HalResource

    @Serializable
    data class WithPrimitivesArray(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @SerialName(value = "names")
        val names: Array<String> = arrayOf("Bolt", "Stiv")

    ) : HalResource, Hal {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as WithPrimitivesArray

            if (self != other.self) return false
            if (user != other.user) return false
            if (!names.contentEquals(other.names)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = self?.hashCode() ?: 0
            result = 31 * result + (user?.hashCode() ?: 0)
            result = 31 * result + names.contentHashCode()
            return result
        }
    }

    @Serializable
    data class LinkListModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @HalLink
        @SerialName(value = "names")
        val names: List<Link> = listOf(
            Link(href = "http://localhost:8008/api/User/1"),
            Link(href = "http://localhost:8008/api/User/2")
        )

    ) : HalResource, Hal

    @Serializable
    data class LinkSetModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @HalLink
        @SerialName(value = "names")
        val names: Set<Link> = setOf(
            Link(href = "http://localhost:8008/api/User/1"),
            Link(href = "http://localhost:8008/api/User/2")
        )

    ) : HalResource, Hal

    @Serializable
    data class LinkArrayModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = OtherModel(
            self = Link(
                href = "http://localhost:8008/api/User/self"
            )
        ),

        @HalLink
        @SerialName(value = "names")
        val names: Array<Link> = arrayOf(
            Link(href = "http://localhost:8008/api/User/1"),
            Link(href = "http://localhost:8008/api/User/2")
        )

    ) : HalResource, Hal {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LinkArrayModel

            if (self != other.self) return false
            if (user != other.user) return false
            if (!names.contentEquals(other.names)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = self?.hashCode() ?: 0
            result = 31 * result + (user?.hashCode() ?: 0)
            result = 31 * result + names.contentHashCode()
            return result
        }
    }

    @Serializable
    data class EmbeddedListModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "users")
        val users: List<OtherModel>? = listOf(
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/1"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/2"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/3"
                )
            )
        )

    ) : HalResource, Hal

    @Serializable
    data class EmbeddedSetModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "users")
        val users: Set<OtherModel>? = setOf(
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/1"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/2"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/3"
                )
            )
        )

    ) : HalResource, Hal

    @Serializable
    data class EmbeddedArrayModel(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "users")
        val users: Array<OtherModel>? = arrayOf(
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/1"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/2"
                )
            ),
            OtherModel(
                self = Link(
                    href = "http://localhost:8008/api/User/3"
                )
            )
        )

    ) : HalResource, Hal {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EmbeddedArrayModel

            if (self != other.self) return false
            if (users != null) {
                if (other.users == null) return false
                if (!users.contentEquals(other.users)) return false
            } else if (other.users != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = self?.hashCode() ?: 0
            result = 31 * result + (users?.contentHashCode() ?: 0)
            return result
        }
    }

    @Serializable
    data class EmbeddedByLink(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = null

    ) : HalResource, Hal

    @Serializable
    data class EmbeddedByLinks(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "users")
        val users: List<OtherModel>? = null

    ) : HalResource, Hal

    @Serializable
    data class EmbeddedByLinkTemplate(

        @HalLink
        @SerialName(value = "self")
        val self: Link? = Link(href = "http://localhost:8008/api/Profile/self"),

        @HalEmbedded
        @SerialName(value = "user")
        val user: OtherModel? = null

    ) : HalResource, Hal
}
