package net.twisterrob.challenges.leetcode2020april.week3.minimum_path

import kotlin.math.min

class Solution {
	fun minPathSum(grid: Array<IntArray>): Int {
		if (grid.isEmpty() || grid[0].isEmpty()) return 0

		// Initialize the top left corner, which is the starting point; no movement possible.
		grid[0][0] = grid[0][0]

		// Initialize first row, where the path can only come from left.
		grid[0].let { firstRow ->
			for (c in 1 until firstRow.size) {
				firstRow[c] = firstRow[c] + firstRow[c - 1]
			}
		}

		// Initialize first column, where the path can only come from above.
		for (r in 1 until grid.size) {
			grid[r][0] = grid[r][0] + grid[r - 1][0]
		}

		// Calculate the rest of the grid by taking the best route from left or above.
		for (r in 1 until grid.size) {
			for (c in 1 until grid[r].size) {
				grid[r][c] = grid[r][c] + min(grid[r - 1][c], grid[r][c - 1])
			}
		}
		return grid.last().last()
	}
}
