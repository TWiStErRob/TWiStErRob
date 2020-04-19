package net.twisterrob.challenges.leetcode2020april.week1.move_zeroes

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: IntArray, expectedResult: IntArray): DynamicTest =
		dynamicTest("${input.toList()} with moved zeroes ${expectedResult.toList()}") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: IntArray, expectedResult: IntArray) {
		Solution().moveZeroes(input)

		assertArrayEquals(expectedResult, input)
	}

	@TestFactory fun example() =
		solutionTest(intArrayOf(0, 1, 0, 3, 12), intArrayOf(1, 3, 12, 0, 0))

	@TestFactory fun `all zeroes`() = arrayOf(
		solutionTest(IntArray(1) { 0 }, IntArray(1) { 0 }),
		solutionTest(IntArray(2) { 0 }, IntArray(2) { 0 }),
		solutionTest(IntArray(3) { 0 }, IntArray(3) { 0 }),
		solutionTest(IntArray(10) { 0 }, IntArray(10) { 0 }),
	)

	@TestFactory fun `edge cases`() = arrayOf(
		solutionTest(intArrayOf(), intArrayOf()),
		solutionTest(intArrayOf(1, 2, 3, 0, 0), intArrayOf(1, 2, 3, 0, 0)),
	)

	@TestFactory fun `no zeroes`() = arrayOf(
		solutionTest(intArrayOf(1, 2, 3, 4, 5), intArrayOf(1, 2, 3, 4, 5)),
		solutionTest(intArrayOf(4, 3, 2, 1), intArrayOf(4, 3, 2, 1)),
	)
}
