package net.twisterrob.challenges.adventOfKotlin2018.week4

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * [Task](https://blog.kotlin-academy.com/advent-of-kotlin-week-4-mocking-cde699ec9963)
 */
class MockingTest {

	interface Example {
		fun getInt(): Int
	}

	@Nested
	inner class Mock {

		private val mock: Example = mock()

		@Test
		fun `can create a mock`() {
			assertNotNull(mock)
		}

		@Test
		fun `can call toString() on a mock`() {
			val result = mock.toString()

			assertEquals("${mock::class.java}@${System.identityHashCode(mock).toString(16)}", result)
		}

		@Test
		fun `can call equals() on a mock`() {
			val result = mock == mock

			assertTrue(result)
		}

		@Test
		fun `can call hashCode() on a mock`() {
			val result = mock.hashCode()

			assertEquals(System.identityHashCode(mock), result)
		}
	}

	@Test
	fun `can set return value of a simple call`() {
		val a: Example = mock()

		setReturnValue({ a.getInt() }, 2)

		assertEquals(2, a.getInt())
	}

	@Test
	fun `can set behavior a simple call`() {
		var i = 1
		val b: Example = mock()

		setBody({ b.getInt() }, { i++ })

		assertEquals(1, b.getInt())
		assertEquals(2, b.getInt())
		assertEquals(3, b.getInt())
	}
}
