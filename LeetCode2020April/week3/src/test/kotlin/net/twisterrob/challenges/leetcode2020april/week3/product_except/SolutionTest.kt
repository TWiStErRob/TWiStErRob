package net.twisterrob.challenges.leetcode2020april.week3.product_except

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: IntArray, expectedResult: IntArray): DynamicTest =
		dynamicTest("${input.toList()} with products except self is ${expectedResult.toList()}") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: IntArray, expectedResult: IntArray) {
		val result = Solution().productExceptSelf(input)

		assertArrayEquals(expectedResult, result)
	}

	@TestFactory fun example() = arrayOf(
		solutionTest(intArrayOf(1, 2, 3, 4), intArrayOf(24, 12, 8, 6)),
	)

	@TestFactory fun zeroes() = arrayOf(
		solutionTest(intArrayOf(0, 0, 0, 0), intArrayOf(0, 0, 0, 0)),
		solutionTest(intArrayOf(0, 0), intArrayOf(0, 0)),
		solutionTest(intArrayOf(0, 1), intArrayOf(1, 0)),
		solutionTest(intArrayOf(1, 0), intArrayOf(0, 1)),
		solutionTest(intArrayOf(2, 0, 3), intArrayOf(0, 6, 0)),
	)

	@TestFactory fun negative() = arrayOf(
		solutionTest(intArrayOf(-1, 1), intArrayOf(1, -1)),
		solutionTest(intArrayOf(-2, -3), intArrayOf(-3, -2)),
		solutionTest(intArrayOf(-2, -3, -4), intArrayOf(12, 8, 6)),
		solutionTest(intArrayOf(-1, 1, -2, 2, -3, 3), intArrayOf(36, -36, 18, -18, 12, -12)),
	)
}
