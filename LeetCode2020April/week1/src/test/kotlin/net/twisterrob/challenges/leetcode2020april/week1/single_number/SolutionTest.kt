package net.twisterrob.challenges.leetcode2020april.week1.single_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(expectedResult: Int, input: IntArray): DynamicTest =
		dynamicTest("${expectedResult} is single in ${input.toList()}") {
			testSolution(expectedResult, input)
		}

	private fun testSolution(expectedResult: Int, input: IntArray) {
		val result = Solution().singleNumber(input)

		assertEquals(expectedResult, result)
	}

	@TestFactory fun `example 1`() =
		solutionTest(1, intArrayOf(2, 2, 1))

	@TestFactory fun `example 2`() =
		solutionTest(4, intArrayOf(4, 1, 2, 1, 2))
}
