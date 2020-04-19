package net.twisterrob.challenges.leetcode2020april.week1.counting_elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: IntArray, expectedResult: Int): DynamicTest =
		DynamicTest.dynamicTest("${input.toList()} has $expectedResult almost duplicate elements") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: IntArray, expectedResult: Int) {
		val result = Solution().countElements(input)

		assertEquals(expectedResult, result)
	}

	@TestFactory fun `example 1`() =
		solutionTest(intArrayOf(1, 2, 3), 2)

	@TestFactory fun `example 2`() =
		solutionTest(intArrayOf(1, 1, 3, 3, 5, 5, 7, 7), 0)

	@TestFactory fun `example 3`() =
		solutionTest(intArrayOf(1, 3, 2, 3, 5, 0), 3)

	@TestFactory fun `example 4`() =
		solutionTest(intArrayOf(1, 1, 2, 2), 2)

	@TestFactory fun `nasty duplicate`() =
		solutionTest(intArrayOf(1, 1, 2), 2)
}
