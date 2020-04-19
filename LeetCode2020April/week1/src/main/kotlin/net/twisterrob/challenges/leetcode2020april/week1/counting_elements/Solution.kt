package net.twisterrob.challenges.leetcode2020april.week1.counting_elements

class Solution {
	fun countElements(arr: IntArray): Int {
		val elements = arr
			// get interesting numbers close to each other in known position
			.sorted()
			// reduce duplication to allow for checking the next item
			.distinct()
			// [1,2,3] -> [(1,2), (2,3)]
			.zipWithNext()
			// check if the adjacent element are adjacent on the number line
			.filter { it.first + 1 == it.second }
			// only look at the element that needs counting
			.map(Pair<Int, Int>::first)
			// store for quick lookup
			.toHashSet()

		return arr.count { it in elements }
	}
}
