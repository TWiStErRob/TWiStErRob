package net.twisterrob.challenges.leetcode2020april.week3.product_except

class Solution {
	fun productExceptSelf(nums: IntArray): IntArray =
		when (nums.count { it == 0 }) {
			0 -> {
				val prod = nums.reduce(Int::times)
				nums.map { prod / it }.toIntArray()
			}

			1 -> {
				val prod = nums.filterNot { it == 0 }.reduce(Int::times)
				nums.map { if (it == 0) prod else 0 }.toIntArray()
			}

			else -> IntArray(nums.size) { 0 }
		}
}
