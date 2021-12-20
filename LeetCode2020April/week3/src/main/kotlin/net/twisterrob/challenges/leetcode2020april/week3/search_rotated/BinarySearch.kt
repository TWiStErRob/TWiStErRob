package net.twisterrob.challenges.leetcode2020april.week3.search_rotated

/**
 * For reference.
 */
class BinarySearch {
	companion object {
		const val NOT_FOUND = -1
	}

	fun search(nums: IntArray, target: Int): Int {
		return search(nums, target, 0, nums.lastIndex)
	}

	/**
	 * nums structure: `[left ... max, min ... right]`
	 */
	private fun search(nums: IntArray, target: Int, left: Int, right: Int): Int {
		if (right < left) return -1
		val mid = left + (right - left) / 2
		when (target) {
			nums[mid] ->
				return mid
			in nums[left]..nums[mid] ->
				return search(nums, target, left, right)
			in nums[mid]..nums[right] ->
				return search(nums, target, left, right)
		}
		return -1
	}
}
