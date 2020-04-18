package net.twisterrob.challenges.leetcode2020april.week3.valid_parenthesis

class Solution {
	/**
	 * @param s length `[1, 100]`, valid characters = `()*`
	 */
	fun checkValidString(s: String): Boolean =
		try {
			checkValidString(s, 0, 0)
			false
		} catch (ex: Success) {
			true
		}

	private class Success : Exception()

	private fun checkValidString(s: String, pos: Int, count: Int) {
		// 3. Left parenthesis '(' must go before the corresponding right parenthesis ')'.
		if (count < 0) return

		// If we reached the end, must make a decision:
		if (pos == s.length) {
			// if parentheses are balanced, we're good,
			if (count == 0)
				throw Success()
			// otherwise this failed, backtrack.
			else
				return
		}

		when (s[pos]) {
			// 1. Any left parenthesis '(' must have a corresponding right parenthesis ')'.
			'(' ->
				checkValidString(s, pos + 1, count + 1)

			// 2. Any right parenthesis ')' must have a corresponding left parenthesis '('.
			')' ->
				checkValidString(s, pos + 1, count - 1)

			// 4. '*' could be treated as
			'*' -> {
				// a single right parenthesis ')'
				checkValidString(s, pos + 1, count - 1)
				// or a single left parenthesis '('
				checkValidString(s, pos + 1, count + 1)
				// or an empty string.
				checkValidString(s, pos + 1, count)
			}
		}
	}
}
