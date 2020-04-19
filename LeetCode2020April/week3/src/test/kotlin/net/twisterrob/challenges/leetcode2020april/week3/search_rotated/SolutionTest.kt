package net.twisterrob.challenges.leetcode2020april.week3.search_rotated

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(target: Int, input: IntArray, expectedResult: Int): DynamicTest =
		dynamicTest("${target} in ${input.toList()} is at position ${expectedResult}") {
			testSolution(target, input, expectedResult)
		}

	private fun testSolution(target: Int, input: IntArray, expectedResult: Int) {
		val result = Solution().search(input, target)

		assertEquals(expectedResult, result)
	}

	@TestFactory fun `example 1`() =
		solutionTest(0, intArrayOf(4, 5, 6, 7, 0, 1, 2), 4)

	@TestFactory fun `example 2`() =
		solutionTest(3, intArrayOf(4, 5, 6, 7, 0, 1, 2), -1)

	@TestFactory fun `old tests`() = arrayOf(
		solutionTest(0, intArrayOf(0), 0),
		solutionTest(0, intArrayOf(0, 1), 0),
		solutionTest(0, intArrayOf(1, 0), 1),
		solutionTest(0, intArrayOf(0, 1, 2), 0),
		solutionTest(0, intArrayOf(2, 0, 1), 1),
		solutionTest(0, intArrayOf(1, 2, 0), 2),
		solutionTest(0, intArrayOf(0, 1, 2, 4, 5, 6), 0),
		solutionTest(0, intArrayOf(6, 0, 1, 2, 4, 5), 1),
		solutionTest(0, intArrayOf(5, 6, 0, 1, 2, 4), 2),
		solutionTest(0, intArrayOf(4, 5, 6, 0, 1, 2), 3),
		solutionTest(0, intArrayOf(2, 4, 5, 6, 0, 1), 4),
		solutionTest(0, intArrayOf(1, 2, 4, 5, 6, 0), 5),
		solutionTest(0, intArrayOf(0, 1, 2, 4, 5, 6, 7), 0),
		solutionTest(0, intArrayOf(7, 0, 1, 2, 4, 5, 6), 1),
		solutionTest(0, intArrayOf(6, 7, 0, 1, 2, 4, 5), 2),
		solutionTest(0, intArrayOf(5, 6, 7, 0, 1, 2, 4), 3),
		solutionTest(0, intArrayOf(4, 5, 6, 7, 0, 1, 2), 4),
		solutionTest(0, intArrayOf(2, 4, 5, 6, 7, 0, 1), 5),
		solutionTest(0, intArrayOf(1, 2, 4, 5, 6, 7, 0), 6),
	)
}
