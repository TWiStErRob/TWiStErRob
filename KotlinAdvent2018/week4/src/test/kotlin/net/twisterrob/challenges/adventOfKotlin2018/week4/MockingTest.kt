package net.twisterrob.challenges.adventOfKotlin2018.week4

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * [Task](https://blog.kotlin-academy.com/advent-of-kotlin-week-4-mocking-cde699ec9963)
 */
class MockingTest {

	interface Example {
		fun getInt(): Int
	}

	@Test
	fun `can set return value of a simple call`() {
		val a = mock<Example>()
		setReturnValue({ a.getInt() }, 2)
		assertEquals(2, a.getInt())
	}

	@Test
	fun `can set behavior a simple call`() {
		var i = 1
		val b = mock<Example>()
		setBody({ b.getInt() }, { i++ })
		assertEquals(1, b.getInt())
		assertEquals(2, b.getInt())
		assertEquals(3, b.getInt())
	}
}
