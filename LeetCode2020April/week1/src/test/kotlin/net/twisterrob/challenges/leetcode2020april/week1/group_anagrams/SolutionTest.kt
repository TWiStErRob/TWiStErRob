package net.twisterrob.challenges.leetcode2020april.week1.group_anagrams

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: Array<String>, expectedResult: List<List<String>>): DynamicTest =
		dynamicTest("${input.toList()} has these anagram groups: $expectedResult") {
			testSolution(input, expectedResult)
		}

	private fun testSolution(input: Array<String>, expectedResult: List<List<String>>) {
		val result = Solution().groupAnagrams(input)

		assertEquals(expectedResult, result)
	}

	@TestFactory fun example() =
		solutionTest(
			arrayOf("eat", "tea", "tan", "ate", "nat", "bat"),
			listOf(
				listOf("eat", "tea", "ate"),
				listOf("tan", "nat"),
				listOf("bat")
			)
		)
}
