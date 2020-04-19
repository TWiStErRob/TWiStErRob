package net.twisterrob.challenges.leetcode2020april.week1.group_anagrams

class Solution {
	fun groupAnagrams(strs: Array<String>): List<List<String>> =
		strs.groupBy(::anagramSignature).values.toList()

	companion object {
		private fun anagramSignature(word: String): String =
			String(word.toCharArray().sortedArray())
	}
}
