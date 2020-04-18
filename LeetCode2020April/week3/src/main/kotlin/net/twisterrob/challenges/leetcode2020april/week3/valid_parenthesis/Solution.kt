package net.twisterrob.challenges.leetcode2020april.week3.valid_parenthesis

class Solution {
    /**
     * @param s length `[1, 100]`, valid characters = `()*`
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun checkValidString(s: String): Boolean {
        val result = s.scan(0) { acc, curr ->
            return@scan when (curr) {
                '(' -> acc + 1
                ')' -> acc - 1
                '*' -> acc
                else -> error("invalid character $curr")
            }
        }
        return result.all { it >= 0 } && result.last() == 0
    }
}
