package net.twisterrob.challenges.leetcode2020april.week3.valid_parenthesis

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class SolutionTest {

	private fun solutionTest(input: String, expectedValid: Boolean): DynamicTest =
		dynamicTest("$input is expected to be ${if (expectedValid) "a valid" else "an invalid"} parenthesis string") {
			testSolution(input, expectedValid)
		}

	private fun testSolution(input: String, expectedValid: Boolean) {
		val result = Solution().checkValidString(input)

		assertEquals(expectedValid, result)
	}

	@Test fun `example 1`() {
		testSolution("()", expectedValid = true)
	}

	@Test fun `example 2`() {
		testSolution("(*)", expectedValid = true)
	}

	@Test fun `example 3`() {
		testSolution("(*))", expectedValid = true)
	}

	@TestFactory fun `edge cases`() = arrayOf(
		solutionTest("", expectedValid = true),
		solutionTest(")(", expectedValid = true),
	)

	@TestFactory fun missingLeft() = arrayOf(
		solutionTest(")", expectedValid = false),
		solutionTest("))", expectedValid = false),
		solutionTest("())", expectedValid = false),
		solutionTest(")()", expectedValid = false),
	)

	@TestFactory fun missingRight() = arrayOf(
		solutionTest("(", expectedValid = false),
		solutionTest("((", expectedValid = false),
		solutionTest("(()", expectedValid = false),
		solutionTest("()(", expectedValid = false),
	)

	@TestFactory fun onlyStars() = arrayOf(
		solutionTest("*".repeat(1), expectedValid = false),
		solutionTest("*".repeat(2), expectedValid = true),
		solutionTest("*".repeat(100), expectedValid = true),
		solutionTest("*".repeat(32), expectedValid = true),
		solutionTest("*".repeat(31), expectedValid = false),
		solutionTest("*".repeat(33), expectedValid = false),
	)

	@TestFactory fun starsComplete() = arrayOf(
		solutionTest("*)", expectedValid = true),
		solutionTest("(*", expectedValid = true),
		solutionTest("**))", expectedValid = true),
		solutionTest("((**", expectedValid = true),
		solutionTest("*())", expectedValid = true),
		solutionTest("(()*", expectedValid = true),
		solutionTest("*)()", expectedValid = true),
		solutionTest("()(*", expectedValid = true),
	)
}
