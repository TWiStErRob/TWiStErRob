package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class SolutionTest {
	private fun solutionTest(input: Int, expectedResult: Boolean) =
		dynamicTest("$input is ${if (expectedResult) "" else " not"} happy") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: Int, expectedResult: Boolean) {
		val result = Solution().isHappy(input)

		assertEquals(expectedResult, result)
	}

	@Test fun example() {
		testSolution(19, true)
	}

	@TestFactory fun `edge cases`() = arrayOf(
		solutionTest(0, false),
		solutionTest(1, true),
	)

	@TestFactory fun `not happy (simple cases)`() = arrayOf(
		solutionTest(2, false),
		solutionTest(3, false /* 9 */),
	)

	@Test fun `full loop`() {
		// (4, 16, 37, 58, 89, 145, 42, 20, 4)
		testSolution(4, false)
	}

	@Test fun `loop starting 2nd`() {
		// 2, (4, 16, 37, 58, 89, 145, 42, 20)
		testSolution(2, false)
	}

	@Test fun `loop starting later`() {
		// 3, 9, 81, 65, 61, (37, 58, 89, 145, 42, 20, 4, 16)
		testSolution(3, false)
	}
}
