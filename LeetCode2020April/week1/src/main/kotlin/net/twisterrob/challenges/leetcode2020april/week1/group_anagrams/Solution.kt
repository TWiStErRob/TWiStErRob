package net.twisterrob.challenges.leetcode2020april.week1.group_anagrams

class Solution {
	fun groupAnagrams(strs: Array<String>): List<List<String>> =
		strs.groupBy { String(it.toCharArray().sortedArray()) }.values.toList()
}
