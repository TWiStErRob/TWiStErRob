package net.twisterrob.challenges.adventOfKotlin2018.week4

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

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

			assertEquals("${mock::class.java.name}@${System.identityHashCode(mock).toString(16)}", result)
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

	@Nested
	inner class Errors {

		private val mock: Example = mock()

		private inline fun <reified T : Throwable> assertExceptionMessageStartsWith(
			message: String, crossinline actions: () -> Unit
		) {
			val ex = assertFailsWith<T> {
				actions()
			}
			if (!ex.message!!.startsWith(message)) {
				fail("`$ex` does not start with `$message`")
			}
		}

		@Test fun `nested setReturnValue`() {
			assertExceptionMessageStartsWith<IllegalStateException>("Already recording") {
				setReturnValue({
					setReturnValue({ mock.getInt() }, 2)
					mock.getInt()
				}, 1)
			}
		}

		@Test fun `nested setBody`() {
			assertExceptionMessageStartsWith<IllegalStateException>("Already recording") {
				setBody({
					setBody({ mock.getInt() }, { 2 })
					mock.getInt()
				}, { 1 })
			}
		}

		@Test fun `setBody in setReturnValue`() {
			assertExceptionMessageStartsWith<IllegalStateException>("Already recording") {
				setReturnValue({
					setBody({ mock.getInt() }, { 2 })
					mock.getInt()
				}, 1)
			}
		}

		@Test fun `setReturnValue in setBody`() {
			assertExceptionMessageStartsWith<IllegalStateException>("Already recording") {
				setBody({
					setReturnValue({ mock.getInt() }, 2)
					mock.getInt()
				}, { 1 })
			}
		}

		@Test fun `missing behavior`() {
			assertExceptionMessageStartsWith<IllegalStateException>("No matching stub found") {
				mock.getInt()
			}
		}

		@Test fun `multiple behavior`() {
			setReturnValue({ mock.getInt() }, 1)
			setReturnValue({ mock.getInt() }, 2)
			assertExceptionMessageStartsWith<IllegalStateException>("Multiple matching stubs found") {
				mock.getInt()
			}
		}
	}

	@Test
	fun `can set return value of a simple call`() {
		val mock: Example = mock()

		setReturnValue({ mock.getInt() }, 2)

		assertEquals(2, mock.getInt())
	}

	@Test
	fun `can set behavior a simple call`() {
		var i = 1
		val mock: Example = mock()

		setBody({ mock.getInt() }) { i++ }

		assertEquals(1, mock.getInt())
		assertEquals(2, mock.getInt())
		assertEquals(3, mock.getInt())
	}
}
