package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class SquaredSumTest {

	private fun squaredSumTest(input: Int, expected: Int) =
		dynamicTest("$input has squared sum of digits: $expected") {
			testSquaredSum(input, expected)
		}

	private fun testSquaredSum(input: Int, expected: Int) {
		val result = input.squaredSum()

		assertEquals(expected, result)
	}

	@TestFactory fun squaredSumOf19Chain() = arrayOf(
		squaredSumTest(19, 1 * 1/*1*/ + 9 * 9/*81*/),
		squaredSumTest(82, 8 * 8/*81*/ + 2 * 2/*4*/),
		squaredSumTest(68, 6 * 6/*36*/ + 8 * 8/*81*/),
		squaredSumTest(100, 1 * 1/*1*/ + 0 * 0/*0*/ + 0 * 0/*0*/),
		squaredSumTest(1, 1 * 1/*1*/),
	)

	@TestFactory fun squaredSumOf2Chain() = arrayOf(
		squaredSumTest(2, 2 * 2/*4*/),
		// loop start
		squaredSumTest(4, 4 * 4/*16*/),
		squaredSumTest(16, 1 * 1/*1*/ + 6 * 6/*36*/),
		squaredSumTest(37, 3 * 3/*9*/ + 7 * 7/*49*/),
		squaredSumTest(58, 5 * 5/*25*/ + 8 * 8/*64*/),
		squaredSumTest(89, 8 * 8/*64*/ + 9 * 9/*81*/),
		squaredSumTest(145, 1 * 1/*1*/ + 4 * 4/*16*/ + 5 * 5/*25*/),
		squaredSumTest(42, 4 * 4/*16*/ + 2 * 2/*4*/),
		squaredSumTest(20, 2 * 2/*4*/ + 0 * 0/*0*/),
		// loop
	)

	@TestFactory fun squaredSumOf3Chain() = arrayOf(
		squaredSumTest(3, 3 * 3/*9*/),
		squaredSumTest(9, 9 * 9/*81*/),
		squaredSumTest(81, 8 * 8/*64*/ + 1 * 1 /*1*/),
		squaredSumTest(65, 6 * 6/*36*/ + 5 * 5 /*25*/),
		squaredSumTest(61, 6 * 6/*36*/ + 1 * 1/*1*/),
		squaredSumTest(37, 3 * 3/*9*/ + 7 * 7/*49*/),
		// loop start
		squaredSumTest(58, 5 * 5/*25*/ + 8 * 8/*64*/),
		squaredSumTest(89, 8 * 8/*64*/ + 9 * 9/*81*/),
		squaredSumTest(145, 1 * 1/*1*/ + 4 * 4/*16*/ + 5 * 5/*25*/),
		squaredSumTest(42, 4 * 4/*16*/ + 2 * 2/*4*/),
		squaredSumTest(20, 2 * 2/*4*/ + 0 * 0/*0*/),
		squaredSumTest(4, 4 * 4/*16*/),
		squaredSumTest(16, 1 * 1/*1*/ + 6 * 6/*36*/),
		squaredSumTest(37, 3 * 3/*9*/ + 7 * 7/*49*/),
		// loop
	)

	@TestFactory fun squaredSum() = arrayOf(
		squaredSumTest(0, 0),
		squaredSumTest(1, 1),
		squaredSumTest(27, 53),
		squaredSumTest(52, 29),
		squaredSumTest(53, 34),
		squaredSumTest(64, 52),
		squaredSumTest(67, 85),
		squaredSumTest(80, 64),
		squaredSumTest(83, 73),
		squaredSumTest(84, 80),
	)
}
