package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class HappySequenceTest {
	private fun happySequenceTest(input: Int, expected: List<Int>, loopAfter: Int?): DynamicTest {
		val s = if (loopAfter != null) "loops after position ${loopAfter}" else "doesn't loop"
		return dynamicTest("$input has happy sequence: $expected, and it $s") {
			testHappySequence(input, expected, loopAfter)
		}
	}

	private fun testHappySequence(input: Int, expected: List<Int>, loopAfter: Int?) {
		val beforeExpected = input.happySequence().take(expected.size)

		assertEquals(expected, beforeExpected.toList()) {
			"Expected list ${expected} appears first."
		}
		if (loopAfter != null) {
			val loop = expected.drop(loopAfter)
			fun loop(repetition: Int) =
				input.happySequence().drop(expected.size).drop(loop.size * repetition)

			val loop1 = loop(0).take(loop.size).toList()
			assertEquals(loop, loop1.toList()) {
				"Loop ${loop} appears again."
			}

			val loop2 = loop(1).take(loop.size).toList()
			assertEquals(loop, loop2.toList()) {
				"Loop ${loop} appears again."
			}

			val loopMore = loop(2)
			assertTrue(loopMore.any()) {
				"Loop hopefully appears more."
			}
		} else {
			val afterExpected = input.happySequence().drop(expected.size)
			assertFalse(afterExpected.any()) {
				"No more elements after the expected items, but was ${afterExpected.take(expected.size).toList()}."
			}
		}
	}

	@TestFactory fun `happy chain of 19`() =
		happySequenceTest(19, listOf(19, 82, 68, 100, 1), loopAfter = null)

	@TestFactory fun `happy chain of 2`() =
		happySequenceTest(2, listOf(2, /*loop start*/4, 16, 37, 58, 89, 145, 42, 20), loopAfter = 1)

	@TestFactory fun `happy chain of 3`() =
		happySequenceTest(3, listOf(3, 9, 81, 65, 61, /*loop start*/37, 58, 89, 145, 42, 20, 4, 16), loopAfter = 5)

	@TestFactory fun `happy chain of 4`() =
		happySequenceTest(4, listOf(/*loop start*/4, 16, 37, 58, 89, 145, 42, 20), loopAfter = 0)
}
