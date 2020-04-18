package net.twisterrob.challenges.leetcode2020april.week1.single_number

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SolutionTest {
	private fun test(expectedResult: Int, input: IntArray) {
		val result = Solution().singleNumber(input)

		assertEquals(expectedResult, result)
	}

	@Test fun `example 1`() {
		test(1, intArrayOf(2, 2, 1))
	}

	@Test fun `example 2`() {
		test(4, intArrayOf(4, 1, 2, 1, 2))
	}
}
