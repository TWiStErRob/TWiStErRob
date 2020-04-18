package net.twisterrob.challenges.leetcode2020april.week1.single_number

class Solution {
	fun singleNumber(nums: IntArray): Int {
		val set = mutableSetOf<Int>()
		nums.forEach { if (it in set) set -= it else set += it }
		return set.single()
	}
}
