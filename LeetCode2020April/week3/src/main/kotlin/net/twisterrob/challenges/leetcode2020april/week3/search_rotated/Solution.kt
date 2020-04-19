package net.twisterrob.challenges.leetcode2020april.week3.search_rotated

class Solution {
	companion object {
		const val NOT_FOUND = -1
	}

	fun search(nums: IntArray, target: Int): Int {
		val largestIndex = nums.toList().zipWithNext().indexOfFirst { (n1, n2) -> n2 < n1 }

		nums.sort()
		val found = nums.binarySearch(target)

		return if (found <= NOT_FOUND) NOT_FOUND else found + largestIndex + 1
	}
}
