package net.twisterrob.challenges.leetcode2020april.week1.happy_number

class Solution {
	fun isHappy(n: Int): Boolean =
		!n.happySequence().hasRepeatingElement()
}

/**
 * Finds if there's a repetition in the sequence. The input has to be in a strict format:
 *  * either all elements are distinct
 *  * or if there's a repetition, that repeats exactly the same elements infinitely
 */
fun Sequence<*>.hasRepeatingElement(): Boolean {
	if (this.take(2).count() < 2) { // take() to prevent infinite termination
		// if we have 0 or 1 elements, there can be no repetition
		return false
	}
	val slow = this.drop(0)
	val fast = this.drop(1).filterIndexed { i, _ -> i % 2 == 0 }
	return slow.zip(fast).any { it.first == it.second }
}

fun Int.happySequence(): Sequence<Int> =
	generateSequence(this) {
		if (it != 1)
			it.squaredSum()
		else
			null // terminate sequence
	}

fun Int.squaredSum(): Int =
	this.digits().sumOf { it * it }

fun Int.digits(): Sequence<Int> =
	when (this@digits) {
		0 ->
			sequenceOf(0)
		else ->
			sequence {
				var n: Int = this@digits
				while (n > 0) {
					yield(n % 10)
					n /= 10
				}
			}
	}
