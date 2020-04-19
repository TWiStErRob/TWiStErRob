package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {
	private fun solutionTest(input: Int, expectedResult: Boolean): DynamicTest =
		dynamicTest("$input is ${if (expectedResult) "" else " not"} happy") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: Int, expectedResult: Boolean) {
		val result = Solution().isHappy(input)

		assertEquals(expectedResult, result)
	}

	@TestFactory fun example() =
		solutionTest(19, true)

	@TestFactory fun `edge cases`() = arrayOf(
		solutionTest(0, false),
		solutionTest(1, true),
	)

	@TestFactory fun `not happy (simple cases)`() = arrayOf(
		solutionTest(2, false),
		solutionTest(3, false /* 9 */),
	)

	@TestFactory fun `full loop`() =
		// (4, 16, 37, 58, 89, 145, 42, 20, 4)
		solutionTest(4, false)

	@TestFactory fun `loop starting 2nd`() =
		// 2, (4, 16, 37, 58, 89, 145, 42, 20)
		solutionTest(2, false)

	@TestFactory fun `loop starting later`() =
		// 3, 9, 81, 65, 61, (37, 58, 89, 145, 42, 20, 4, 16)
		solutionTest(3, false)
}
