package net.twisterrob.challenges.leetcode2020april.week3.search_rotated

class ManualSolution {
	// each nested if decides enough information as is printed inside
	// there are no dependencies between nested else-ifs
	fun search(nums: IntArray, target: Int): Int {
		// TODO this is unnecessary, but without it the not found case can go into infinite loop.
		if (target !in nums) return -1
		//System.out.printf("%nSearching for %d in %s%n", target, Arrays.toString(nums));
		var left = 0
		var right = nums.size - 1
		while (left <= right) {
			val mid = left + (right - left) / 2
			//System.out.printf("left: %d @%d, mid: %d @%d, right: %d @%d -> ", nums[left], left, nums[mid], mid, nums[right], right);
			when {
				target == nums[mid] -> {
					//System.out.println("found");
					return mid
				}

				target < nums[mid] ->
					when {
						nums[left] <= target -> {
							//System.out.println("left ... target ... mid .?. right");
							right = mid - 1
						}

						nums[mid] < nums[right] -> {
							//System.out.println("left .?. max min ... target ... mid ... right");
							right = mid - 1
						}

						target <= nums[right] -> {
							//System.out.println("left ... mid ... max min ... target ... right");
							left = mid + 1
						}
					}

				target > nums[mid] ->
					when {
						target <= nums[right] -> {
							//System.out.println("left .?. mid ... target ... right");
							left = mid + 1
						}

						nums[left] < nums[mid] -> {
							//System.out.println("left ... mid ... target ... max min .?. right");
							left = mid + 1
						}

						nums[left] <= target -> {
							//System.out.println("left ... target ... max min ... mid ... right");
							right = mid - 1
						}
					}
			}
		}
		//System.out.printf("Finished %d @%d - %d @%d%n", nums[left], left, nums[right], right);
		return -1
	}
}
