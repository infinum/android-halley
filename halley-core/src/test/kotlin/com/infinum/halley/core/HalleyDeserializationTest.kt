package com.infinum.halley.core

import com.infinum.halley.core.serializers.link.models.Link
import com.infinum.halley.core.serializers.link.models.templated.params.Arguments
import com.infinum.halley.core.server.TestWebServer
import com.infinum.halley.core.shared.ResourceLoader
import com.infinum.halley.core.shared.models.Empty
import com.infinum.halley.core.shared.models.Hal
import com.infinum.halley.core.shared.models.Primitive
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Suppress("MaxLineLength") // We favor descriptive test names.
@DisplayName("Halley deserialization")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HalleyDeserializationTest {

    private val testServer = TestWebServer()

    @BeforeAll
    fun beforeAll() {
        testServer.start()
    }

    @Nested
    @DisplayName("Plain deserialization")
    inner class PlainDeserializationTest {

        @Test
        fun `Empty JSON deserializes into empty object`() {
            val halley = Halley()

            val expected = Empty.Model
            val actual: Empty.Model = halley.decodeFromString(ResourceLoader("empty_model.json"))

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty list`() {
            val halley = Halley()

            val expected = Empty.ListModel()
            val actual: Empty.ListModel =
                halley.decodeFromString(ResourceLoader("empty_collection.json"))

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty set`() {
            val halley = Halley()

            val expected = Empty.SetModel()
            val actual: Empty.SetModel =
                halley.decodeFromString(ResourceLoader("empty_collection.json"))

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty array`() {
            val halley = Halley()

            val expected = Empty.ArrayModel()
            val actual: Empty.ArrayModel =
                halley.decodeFromString(ResourceLoader("empty_collection.json"))

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with primitive values deserializes into primitives object`() {
            val halley = Halley()

            val expected = Primitive.Model()
            val actual: Primitive.Model = halley.decodeFromString(
                ResourceLoader("primitives_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of primitive values deserializes into primitives list`() {
            val halley = Halley()

            val expected = Primitive.ListModel()
            val actual: Primitive.ListModel = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of primitive values deserializes into primitives set`() {
            val halley = Halley()

            val expected = Primitive.SetModel()
            val actual: Primitive.SetModel = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of primitive values deserializes into primitives array`() {
            val halley = Halley()

            val expected = Primitive.ArrayModel()
            val actual: Primitive.ArrayModel = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("HAL deserialization")
    inner class HalDeserializationTest {

        @Test
        fun `Empty JSON deserializes into empty HAL resource`() {
            val halley = Halley()

            val expected = Empty.HalModel()
            val actual: Empty.HalModel = halley.decodeFromString(
                ResourceLoader("empty_model.json")
            )

            assertInstanceOf(expected::class.java, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty HAL resource list`() {
            val halley = Halley()

            val expected = Empty.HalList()
            val actual: Empty.HalList = halley.decodeFromString(
                ResourceLoader("empty_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty HAL resource set`() {
            val halley = Halley()

            val expected = Empty.HalSet()
            val actual: Empty.HalSet = halley.decodeFromString(
                ResourceLoader("empty_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty JSON array deserializes into empty HAL resource array`() {
            val halley = Halley()

            val expected = Empty.HalArray()
            val actual: Empty.HalArray = halley.decodeFromString(
                ResourceLoader("empty_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with primitives deserializes into primitive HAL resource`() {
            val halley = Halley()

            val expected = Hal.Primitive()
            val actual: Hal.Primitive = halley.decodeFromString(
                ResourceLoader("primitives_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with links and embedded deserializes into HAL resource`() {
            val halley = Halley()

            val expected = Hal.Model()
            val actual: Hal.Model = halley.decodeFromString(
                ResourceLoader("hal_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with primitives, links and embedded deserializes into HAL resource with primitives`() {
            val halley = Halley()

            val expected = Hal.WithPrimitives()
            val actual: Hal.WithPrimitives = halley.decodeFromString(
                ResourceLoader("hal_with_primitives_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of primitives, links and embedded deserializes into HAL resource with list of primitives`() {
            val halley = Halley()

            val expected = Hal.WithPrimitivesList()
            val actual: Hal.WithPrimitivesList = halley.decodeFromString(
                ResourceLoader("hal_with_primitives_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of primitives, links and embedded deserializes into HAL resource with set of primitives`() {
            val halley = Halley()

            val expected = Hal.WithPrimitivesSet()
            val actual: Hal.WithPrimitivesSet = halley.decodeFromString(
                ResourceLoader("hal_with_primitives_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of primitives, links and embedded deserializes into HAL resource with array of primitives`() {
            val halley = Halley()

            val expected = Hal.WithPrimitivesArray()
            val actual: Hal.WithPrimitivesArray = halley.decodeFromString(
                ResourceLoader("hal_with_primitives_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of links and embedded deserializes into HAL resource with list of links`() {
            val halley = Halley()

            val expected = Hal.LinkListModel()
            val actual: Hal.LinkListModel = halley.decodeFromString(
                ResourceLoader("hal_with_link_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of links and embedded deserializes into HAL resource with set of links`() {
            val halley = Halley()

            val expected = Hal.LinkSetModel()
            val actual: Hal.LinkSetModel = halley.decodeFromString(
                ResourceLoader("hal_with_link_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with array of links and embedded deserializes into HAL resource with array of links`() {
            val halley = Halley()

            val expected = Hal.LinkArrayModel()
            val actual: Hal.LinkArrayModel = halley.decodeFromString(
                ResourceLoader("hal_with_link_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with links and array of embedded deserializes into HAL resource with list of embedded`() {
            val halley = Halley()

            val expected = Hal.EmbeddedListModel()
            val actual: Hal.EmbeddedListModel = halley.decodeFromString(
                ResourceLoader("hal_with_embedded_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with links and array of embedded deserializes into HAL resource with set of embedded`() {
            val halley = Halley()

            val expected = Hal.EmbeddedSetModel()
            val actual: Hal.EmbeddedSetModel = halley.decodeFromString(
                ResourceLoader("hal_with_embedded_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with links and array of embedded deserializes into HAL resource with array of embedded`() {
            val halley = Halley()

            val expected = Hal.EmbeddedArrayModel()
            val actual: Hal.EmbeddedArrayModel = halley.decodeFromString(
                ResourceLoader("hal_with_embedded_collection_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with link, without embedded and without options deserializes into HAL resource with embedded by link`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLink(
                user = Hal.OtherModel(
                    self = Link(href = "http://localhost:8008/api/User/1"),
                    name = "Nika"
                )
            )
            val actual: Hal.EmbeddedByLink = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_link_model.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with links, without embedded and without options deserializes into HAL resource with multiple embedded by links`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLinks(
                users = listOf(
                    Hal.OtherModel(
                        self = Link(href = "http://localhost:8008/api/User/1"),
                        name = "Nika"
                    ),
                    Hal.OtherModel(
                        self = Link(href = "http://localhost:8008/api/User/2"),
                        name = "Petra"
                    )
                )
            )
            val actual: Hal.EmbeddedByLinks = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_links_model.json")
            )

            assertInstanceOf(expected::class.java, actual)
            assertEquals(expected.self, actual.self)
            assertEquals(expected.users?.size, actual.users?.size)
            assertTrue(actual.users?.contains(expected.users?.first()) ?: false)
            assertTrue(actual.users?.contains(expected.users?.last()) ?: false)
        }

        @Test
        fun `JSON with link template, without embedded and with options deserializes into HAL resource with embedded by link`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLinkTemplate(
                user = Hal.OtherModel(
                    self = Link(href = "http://localhost:8008/api/User/1"),
                    name = "Nika"
                )
            )
            val actual: Hal.EmbeddedByLinkTemplate = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_link_template_model.json"),
                Halley.Options(
                    template = Arguments.Template(
                        mapOf("user" to mapOf("id" to "1"))
                    )
                )
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with link, without embedded and with query options deserializes into HAL resource with embedded by link`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLink(
                user = Hal.OtherModel(
                    self = Link(href = "http://localhost:8008/api/User/1"),
                    name = "Nika"
                )
            )
            val actual: Hal.EmbeddedByLink = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_link_query_model.json"),
                Halley.Options(
                    query = Arguments.Query(
                        mapOf("user" to mapOf("verified" to "true"))
                    )
                )
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with link, without embedded and with common options deserializes into HAL resource with embedded by link`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLink(
                user = Hal.OtherModel(
                    self = Link(href = "http://localhost:8008/api/User/1"),
                    name = "Nika"
                )
            )
            val actual: Hal.EmbeddedByLink = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_link_query_model.json"),
                Halley.Options(
                    common = Arguments.Common(
                        mapOf("approved" to "false")
                    )
                )
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON with link, without embedded and with all options deserializes into HAL resource with embedded by link`() {
            val halley = Halley()

            val expected = Hal.EmbeddedByLinkTemplate(
                user = Hal.OtherModel(
                    self = Link(href = "http://localhost:8008/api/User/1"),
                    name = "Nika"
                )
            )
            val actual: Hal.EmbeddedByLinkTemplate = halley.decodeFromString(
                ResourceLoader("hal_embedded_by_link_common_query_template_model.json"),
                Halley.Options(
                    common = Arguments.Common(
                        mapOf("approved" to "false")
                    ),
                    query = Arguments.Query(
                        mapOf("user" to mapOf("verified" to "true"))
                    ),
                    template = Arguments.Template(
                        mapOf("user" to mapOf("id" to "1"))
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("Combined plain and HAL deserialization")
    inner class CombinedDeserializationTest {

        @Test
        fun `JSON array of primitives deserializes into HAL resource with list of primitives`() {
            val halley = Halley()

            val expected = Hal.ListOfPrimitives()
            val actual: Hal.ListOfPrimitives = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of primitives deserializes into HAL resource with set of primitives`() {
            val halley = Halley()

            val expected = Hal.SetOfPrimitives()
            val actual: Hal.SetOfPrimitives = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of primitives deserializes into HAL resource with array of primitives`() {
            val halley = Halley()

            val expected = Hal.ArrayOfPrimitives()
            val actual: Hal.ArrayOfPrimitives = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of HAL resources deserializes into primitive list of HAL resources`() {
            val halley = Halley()

            val expected = Primitive.ListOfHal()
            val actual: Primitive.ListOfHal = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of HAL resources deserializes into primitive set of HAL resources`() {
            val halley = Halley()

            val expected = Primitive.SetOfHal()
            val actual: Primitive.SetOfHal = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `JSON array of HAL resources deserializes into primitive array of HAL resources`() {
            val halley = Halley()

            val expected = Primitive.ArrayOfHal()
            val actual: Primitive.ArrayOfHal = halley.decodeFromString(
                ResourceLoader("primitives_collection.json")
            )

            assertEquals(expected, actual)
        }
    }

    @AfterAll
    fun afterAll() {
        testServer.stop()
    }
}
