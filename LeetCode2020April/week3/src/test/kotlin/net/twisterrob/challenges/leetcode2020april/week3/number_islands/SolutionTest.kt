package net.twisterrob.challenges.leetcode2020april.week3.number_islands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: String, expectedIslands: Int): DynamicTest =
		dynamicTest("Expected to have $expectedIslands islands:\n${input.replace("\n", "\\n\n")}") {
			testSolution(input, expectedIslands)
		}

	private fun testSolution(input: String, expectedIslands: Int) {
		fun toLeetCodeChar(char: Char): Char =
			when (char) {
				'▮' -> '1'
				'▯' -> '0'
				else -> error(char)
			}

		val inputArray = input
			.lineSequence()
			.map { it.map(::toLeetCodeChar).toCharArray() }
			.toList()
			.toTypedArray()

		val result = Solution().numIslands(inputArray)

		assertEquals(expectedIslands, result)
	}

	@TestFactory fun `example 1`() =
		solutionTest(
			"""
			▮▮▮▮▯
			▮▮▯▮▯
			▮▮▯▯▯
			▯▯▯▯▯
			""".trimIndent(),
			1
		)

	@TestFactory fun `example 2`() =
		solutionTest(
			"""
			▮▮▯▯▯
			▮▮▯▯▯
			▯▯▮▯▯
			▯▯▯▮▮
			""".trimIndent(),
			3
		)

	@TestFactory fun empty() = arrayOf(
		solutionTest(
			"""
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯▯
			▯▯
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯▯
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯
			▯
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯▯▯
			▯▯▯
			▯▯▯
			""".trimIndent(),
			0
		),
		solutionTest(
			"""
			▯▯▯▯
			▯▯▯▯
			▯▯▯▯
			▯▯▯▯
			""".trimIndent(),
			0
		),
	)

	@TestFactory fun full() = arrayOf(
		solutionTest(
			"""
			▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮
			▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮▮
			▮▮▮
			▮▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮▮▮
			▮▮▮▮
			▮▮▮▮
			▮▮▮▮
			""".trimIndent(),
			1
		),
	)

	@TestFactory fun checkerboard() = arrayOf(
		solutionTest(
			"""
			▮▯
			▯▮
			""".trimIndent(),
			2
		),
		solutionTest(
			"""
			▯▮
			▮▯
			""".trimIndent(),
			2
		),
		solutionTest(
			"""
			▮▯▮
			▯▮▯
			▮▯▮
			""".trimIndent(),
			5
		),
		solutionTest(
			"""
			▯▮▯
			▮▯▮
			▯▮▯
			""".trimIndent(),
			4
		),
		solutionTest(
			"""
			▮▯▮▯▮▯▮▯
			▯▮▯▮▯▮▯▮
			▮▯▮▯▮▯▮▯
			▯▮▯▮▯▮▯▮
			▮▯▮▯▮▯▮▯
			▯▮▯▮▯▮▯▮
			▮▯▮▯▮▯▮▯
			▯▮▯▮▯▮▯▮
			""".trimIndent(),
			32
		),
	)

	@TestFactory fun rows() = arrayOf(
		solutionTest(
			"""
			▯▯
			▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮
			▯▯
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▯▯
			▮▮
			▯▯
			▮▮
			""".trimIndent(),
			2
		),
		solutionTest(
			"""
			▮▮
			▯▯
			▮▮
			▯▯
			""".trimIndent(),
			2
		),
		solutionTest(
			"""
			▯▯▯▯▯▯▯▯▯▯
			▮▮▮▮▮▮▮▮▮▮
			▯▯▯▯▯▯▯▯▯▯
			▮▮▮▮▮▮▮▮▮▮
			▯▯▯▯▯▯▯▯▯▯
			""".trimIndent(),
			2
		),
	)

	@TestFactory fun columns() = arrayOf(
		solutionTest(
			"""
			▮▯
			▮▯
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▯▯
			▮▮
			▯▯
			▮▮
			""".trimIndent(),
			2
		),
		solutionTest(
			"""
			▯▯▯▯▯▯▯▯▯▯
			▮▮▮▮▮▮▮▮▮▮
			▯▯▯▯▯▯▯▯▯▯
			▮▮▮▮▮▮▮▮▮▮
			▯▯▯▯▯▯▯▯▯▯
			""".trimIndent(),
			2
		),
	)

	@TestFactory fun edges() = arrayOf(
		solutionTest(
			"""
			▮▮▮
			▮▯▮
			▮▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮▮
			▮▯▮
			▮▯▮
			▮▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮▮▮
			▮▯▯▮
			▮▮▮▮
			""".trimIndent(),
			1
		),
		solutionTest(
			"""
			▮▮▮▮▮▮
			▮▯▯▯▯▮
			▮▯▯▯▯▮
			▮▯▯▯▯▮
			▮▮▮▮▮▮
			""".trimIndent(),
			1
		),
	)
}
