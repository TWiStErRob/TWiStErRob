package net.twisterrob.challenges.leetcode2020april.week1.move_zeroes

class Solution {
	fun moveZeroes(nums: IntArray) {
		var targetIndex = 0

		// compact non-zero numbers at the beginning
		nums
			.asSequence()
			.filterNot { it == 0 }
			.forEach { n -> nums[targetIndex++] = n }

		// fill end with zeroes
		nums
			.indices
			.drop(targetIndex)
			.forEach { zeroIndex -> nums[zeroIndex] = 0 }
	}
}
