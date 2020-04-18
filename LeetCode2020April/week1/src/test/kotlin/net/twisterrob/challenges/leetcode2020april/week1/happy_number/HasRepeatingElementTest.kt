package net.twisterrob.challenges.leetcode2020april.week1.happy_number

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class HasRepeatingElementTest {

	@TestFactory fun `distinct sequences don't have repeating elements`() = arrayOf(
		hasRepeatingElementTest(false, 1),
		hasRepeatingElementTest(false, 1, 2),
		hasRepeatingElementTest(false, 1, 2, 3),
		hasRepeatingElementTest(false, 1, 2, 3, 4),
		hasRepeatingElementTest(false, 1, 2, 3, 4, 5),
	)

	@TestFactory fun `uniform sequences have repeating elements`() = arrayOf(
		hasRepeatingElementTest(true, 1, 1),
		hasRepeatingElementTest(true, 1, 1, 1),
		hasRepeatingElementTest(true, 1, 1, 1, 1),
		hasRepeatingElementTest(true, 1, 1, 1, 1, 1, 1),
		hasRepeatingElementTest(true, 1, 2, 1, 2, 1, 2),
		hasRepeatingElementTest(true, 1, 2, 3, 1, 2, 3, 1, 2, 3),
	)

	@TestFactory fun `alternating sequences have repeating elements`() = arrayOf(
		hasRepeatingElementTest(true, 1, 2, 1, 2),
		hasRepeatingElementTest(true, 1, 2, 1, 2, 1, 2),
		hasRepeatingElementTest(true, 1, 2, 1, 2, 1, 2, 1, 2),
	)

	@TestFactory fun `repeating sequences have repeating elements`() = arrayOf(
		hasRepeatingElementTest(true, 1, 2, 3, 1, 2, 3),
		hasRepeatingElementTest(true, 1, 2, 3, 1, 2, 3, 1, 2, 3),
		hasRepeatingElementTest(true, 1, 2, 3, 4, 1, 2, 3, 4),
		hasRepeatingElementTest(true, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5),
	)

	@TestFactory fun `infinite repeating sequences have repeating elements`() = arrayOf(
		hasRepeatingElementTest(true, generateSequence(0) { 1 }),
		hasRepeatingElementTest(true, generateSequence(0, object : Function1<Int, Int> {
			private var flag = false
			override fun invoke(ignored: Int): Int {
				flag = !flag
				return if (flag) 1 else 2
			}
		})),
	)

	private fun hasRepeatingElementTest(hasRepeating: Boolean, vararg elements: Int) =
		dynamicTest("sequence of ${elements.toList()} has repeating elements: $hasRepeating") {
			testHasRepeatingElement(hasRepeating, elements.asSequence())
		}

	private fun hasRepeatingElementTest(hasRepeating: Boolean, input: Sequence<Int>) =
		dynamicTest("sequence of ${input} has repeating elements: $hasRepeating") {
			testHasRepeatingElement(hasRepeating, input)
		}

	private fun testHasRepeatingElement(hasRepeating: Boolean, input: Sequence<Int>) {
		val result = input.hasRepeatingElement()

		assertEquals(hasRepeating, result)
	}
}
