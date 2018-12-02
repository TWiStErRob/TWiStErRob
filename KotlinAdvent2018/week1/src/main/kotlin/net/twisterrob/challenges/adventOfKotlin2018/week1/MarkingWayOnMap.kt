package net.twisterrob.challenges.adventOfKotlin2018.week1

import net.twisterrob.challenges.adventOfKotlin2018.week1.AStar.Pos
import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell
import java.lang.Math.sqrt

fun addPath(inputMap: String): String {
	val map = Map.parse(inputMap)
	AStar(map).run(::heuristicBetween, Map::distanceBetween)
	return map.toString()
}

private fun heuristicBetween(a: Pos, b: Pos): Double {
	fun Int.squared() = (this * this).toDouble()
	return sqrt((a.row - b.row).squared() + (a.col - b.col).squared())
}


private const val selfPositionRow = 1
private const val selfPositionCol = 1
private val distancesForNeighbors = arrayOf(
	arrayOf(1.5, 1.0, 1.5),
	arrayOf(1.0, 0.0, 1.0),
	arrayOf(1.5, 1.0, 1.5)
)
private fun Map.distanceBetween(a: Pos, b: Pos): Double {
	if (this[a.row, a.col] == Cell.Obstacle || this[b.row, b.col] == Cell.Obstacle) {
		return Double.POSITIVE_INFINITY
	}
	val rowD = a.row - b.row
	val colD = a.col - b.col
	require(rowD in -1..1) { "not neighboring cells" }
	require(colD in -1..1) { "not neighboring cells" }
	require(!(rowD == 0 && colD == 0)) { "distance to self shouldn't be asked" }
	return distancesForNeighbors[selfPositionRow - rowD][selfPositionCol - colD]
}
