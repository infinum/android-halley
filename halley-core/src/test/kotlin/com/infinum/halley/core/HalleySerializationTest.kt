package com.infinum.halley.core

import com.infinum.halley.core.shared.ResourceLoader
import com.infinum.halley.core.shared.models.Empty
import com.infinum.halley.core.shared.models.Hal
import com.infinum.halley.core.shared.models.Primitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Suppress("MaxLineLength") // We favor descriptive test names.
@DisplayName("Halley serialization")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HalleySerializationTest {

    @Nested
    @DisplayName("Plain serialization")
    inner class PlainSerializationTest {

        @Test
        fun `Empty object serializes into empty JSON`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_model.json")
            val actual = halley.encodeToString(Empty.Model)

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty list serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.ListModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty set serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.SetModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty array serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.ArrayModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitives object serializes into JSON with primitive values`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_model.json")
            val actual = halley.encodeToString(Primitive.Model())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitives list serializes into JSON array of primitive values`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.ListModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitives set serializes into JSON array of primitive values`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.SetModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitives array serializes into JSON array of primitive values`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.ArrayModel())

            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("HAL serialization")
    inner class HalSerializationTest {

        @Test
        fun `Empty HAL resource serializes into empty JSON`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_model.json")
            val actual = halley.encodeToString(Empty.HalModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty HAL resource list serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.HalList())

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty HAL resource set serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.HalSet())

            assertEquals(expected, actual)
        }

        @Test
        fun `Empty HAL resource array serializes into empty JSON array`() {
            val halley = Halley()

            val expected = ResourceLoader("empty_collection.json")
            val actual = halley.encodeToString(Empty.HalArray())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitive HAL resource serializes into JSON with primitives`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_model.json")
            val actual = halley.encodeToString(Hal.Primitive())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource serializes into JSON with links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_model.json")
            val actual = halley.encodeToString(Hal.Model())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with primitives serializes into JSON with primitives, links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_primitives_model.json")
            val actual = halley.encodeToString(Hal.WithPrimitives())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with list of primitives serializes into JSON with list of primitives, links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_primitives_collection_model.json")
            val actual = halley.encodeToString(Hal.WithPrimitivesList())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with set of primitives serializes into JSON with list of primitives, links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_primitives_collection_model.json")
            val actual = halley.encodeToString(Hal.WithPrimitivesSet())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with array of primitives serializes into JSON with list of primitives, links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_primitives_collection_model.json")
            val actual = halley.encodeToString(Hal.WithPrimitivesArray())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with list of links serializes into JSON with list of links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_link_collection_model.json")
            val actual = halley.encodeToString(Hal.LinkListModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with set of links serializes into JSON with list of links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_link_collection_model.json")
            val actual = halley.encodeToString(Hal.LinkSetModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with array of links serializes into JSON with list of links and embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_link_collection_model.json")
            val actual = halley.encodeToString(Hal.LinkArrayModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with list of embedded serializes into JSON with links and array of embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_embedded_collection_model.json")
            val actual = halley.encodeToString(Hal.EmbeddedListModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with set of embedded serializes into JSON with links and array of embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_embedded_collection_model.json")
            val actual = halley.encodeToString(Hal.EmbeddedSetModel())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with array of embedded serializes into JSON with links and array of embedded`() {
            val halley = Halley()

            val expected = ResourceLoader("hal_with_embedded_collection_model.json")
            val actual = halley.encodeToString(Hal.EmbeddedArrayModel())

            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("Combined plain and HAL serialization")
    inner class CombinedSerializationTest {

        @Test
        fun `HAL resource with list of primitives serializes into JSON array of primitives`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Hal.ListOfPrimitives())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with set of primitives serializes into JSON array of primitives`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Hal.SetOfPrimitives())

            assertEquals(expected, actual)
        }

        @Test
        fun `HAL resource with array of primitives serializes into JSON array of primitives`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Hal.ArrayOfPrimitives())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitive list of HAL resources serializes into JSON array of HAL resources`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.ListOfHal())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitive set of HAL resources serializes into JSON array of HAL resources`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.SetOfHal())

            assertEquals(expected, actual)
        }

        @Test
        fun `Primitive array of HAL resources serializes into JSON array of HAL resources`() {
            val halley = Halley()

            val expected = ResourceLoader("primitives_collection.json")
            val actual = halley.encodeToString(Primitive.ArrayOfHal())

            assertEquals(expected, actual)
        }
    }
}
