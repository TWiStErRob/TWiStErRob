package net.twisterrob.challenges.leetcode2020april.week3.valid_parenthesis

class Solution {
	/**
	 * @param s length `[1, 100]`, valid characters = `()*`
	 */
	fun checkValidString(s: String): Boolean {
		var acc = 0
		var jokers = 0
		for (curr in s) {
			when (curr) {
				'(' -> acc += 1
				')' -> acc -= 1
				'*' -> jokers += 1
				else -> error("invalid character $curr")
			}
			// violates 3.
			if (acc < 0) {
				if (jokers > 0) {
					// but we can recover by using * as opening because of 4.
					jokers -= 1
					acc += 1
				} else {
					return false
				}
			}
		}
		while (acc > 0 && jokers > 0) {
			// close any remaining parentheses by 4.
			acc -= 1
			jokers -= 1
		}
		return acc == 0
	}
}
