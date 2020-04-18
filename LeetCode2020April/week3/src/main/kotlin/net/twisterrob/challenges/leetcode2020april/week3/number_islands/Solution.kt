package net.twisterrob.challenges.leetcode2020april.week3.number_islands

class Solution {

	fun numIslands(grid: Array<CharArray>): Int {
		val islands = Array(grid.size) { r ->
			IntArray(grid[0].size) { c ->
				when (grid[r][c]) {
					'1' -> Marker.UNPROCESSED_LAND
					'0' -> Marker.WATER
					else -> error("Invalid character at position $r, $c: '${grid[r][c]}'")
				}
			}
		}
		val marker = Marker(islands)
		marker.markIslands()
		return marker.islandCount
	}
}

private class Marker(
	private val islands: Array<IntArray>
) {

	companion object {
		const val UNPROCESSED_LAND = -1
		const val WATER = 0

		// anything above 0 is a marked island
		private const val FIRST_ISLAND = 1
	}

	private var nextIsland = FIRST_ISLAND

	val islandCount: Int get() = nextIsland - 1

	@Suppress("LoopToCallChain")
	fun markIslands() {
		// Walk the map and traverse the full island when unprocessed land found.
		for (r in islands.indices) {
			for (c in islands[r].indices) {
				if (islands[r][c] == UNPROCESSED_LAND) {
					markIsland(r, c, island = nextIsland++)
				}
			}
		}
	}

	private fun markIsland(r: Int, c: Int, island: Int) {
		if (r < 0 || islands.size <= r) return // outside the map -> water -> nothing to do
		if (c < 0 || islands[r].size <= c) return // outside the map -> water -> nothing to do
		when (islands[r][c]) {
			UNPROCESSED_LAND -> {
				islands[r][c] = island
				markIsland(r - 1, c, islands[r][c])
				markIsland(r + 1, c, islands[r][c])
				markIsland(r, c - 1, islands[r][c])
				markIsland(r, c + 1, islands[r][c])
			}

			WATER -> return // nothing to do
			else -> return // already processed
		}
	}
}
