package net.twisterrob.challenges.leetcode2020april.week3.minimum_path

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: Array<IntArray>, expectedSum: Int): DynamicTest =
		dynamicTest("Best path is $expectedSum on ${input.map { it.toList() }}") {
			testSolution(input, expectedSum)
		}

	private fun testSolution(input: Array<IntArray>, expectedSum: Int) {
		val result = Solution().minPathSum(input)

		assertEquals(expectedSum, result)
	}

	@TestFactory fun example() =
		solutionTest(
			arrayOf(
				intArrayOf(1, 3, 1),
				intArrayOf(1, 5, 1),
				intArrayOf(4, 2, 1),
			),
			7
		)

	@TestFactory fun uniform() = arrayOf(
		solutionTest(
			arrayOf(
			),
			0
		),
		solutionTest(
			arrayOf(
				intArrayOf(0),
			),
			0
		),
		solutionTest(
			arrayOf(
				intArrayOf(1),
			),
			1
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 1),
				intArrayOf(1, 1),
			),
			3
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 1, 1),
				intArrayOf(1, 1, 1),
				intArrayOf(1, 1, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(2, 2),
				intArrayOf(2, 2),
				intArrayOf(2, 2),
			),
			8
		),
		solutionTest(
			arrayOf(
				intArrayOf(2, 2, 2),
				intArrayOf(2, 2, 2),
			),
			8
		),
	)

	@TestFactory fun patterns() = arrayOf(
		solutionTest(
			arrayOf(
				intArrayOf(1, 1, 1),
				intArrayOf(2, 2, 1),
				intArrayOf(2, 2, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 2, 2),
				intArrayOf(1, 2, 2),
				intArrayOf(1, 1, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 2, 2),
				intArrayOf(1, 1, 2),
				intArrayOf(2, 1, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 1, 2),
				intArrayOf(2, 1, 1),
				intArrayOf(2, 2, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 2, 2),
				intArrayOf(1, 1, 1),
				intArrayOf(2, 2, 1),
			),
			5
		),
		solutionTest(
			arrayOf(
				intArrayOf(1, 1, 2),
				intArrayOf(2, 1, 2),
				intArrayOf(2, 1, 1),
			),
			5
		),
	)
}
