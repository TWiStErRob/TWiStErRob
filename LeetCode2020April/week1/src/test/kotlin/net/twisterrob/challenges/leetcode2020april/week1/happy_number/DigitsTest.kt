package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class DigitsTest {
	private fun digitsTest(input: Int, expected: List<Int>) =
		dynamicTest("$input has digits: $expected") {
			testDigits(input, expected)
		}

	private fun testDigits(input: Int, expected: List<Int>) {
		val result = input.digits().toList().reversed()

		assertEquals(expected.toList(), result)
	}

	@TestFactory fun `single digits`() =
		(0..9).map { digitsTest(it, listOf(it)) }

	@TestFactory fun `double digits`() =
		(1..9).flatMap { first -> (0..9).map { second -> first to second } }
			.map { (first, second) -> digitsTest(first * 10 + second, listOf(first, second)) }

	@TestFactory fun `longer numbers`() = arrayOf(
		digitsTest(100, listOf(1, 0, 0)),
		digitsTest(101, listOf(1, 0, 1)),
		digitsTest(102, listOf(1, 0, 2)),
		digitsTest(256, listOf(2, 5, 6)),
		digitsTest(999, listOf(9, 9, 9)),
		digitsTest(1024, listOf(1, 0, 2, 4)),
		digitsTest(123123, listOf(1, 2, 3, 1, 2, 3)),
		digitsTest(Integer.MAX_VALUE, listOf(2, 1, 4, 7, 4, 8, 3, 6, 4, 7)),
	)
}
