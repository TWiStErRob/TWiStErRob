package net.twisterrob.challenges.leetcode2020april.week1.move_zeroes

class Solution {
	fun moveZeroes(nums: IntArray) {
		val nonZero = nums.filterNot { it == 0 }.toIntArray()
		// nums[beginning] = nonZero[..]
		System.arraycopy(nonZero, 0, nums, 0, nonZero.size)
		// nums[after nonZero] = 0
		nums.indices.drop(nonZero.size).forEach { nums[it] = 0 }
	}
}
