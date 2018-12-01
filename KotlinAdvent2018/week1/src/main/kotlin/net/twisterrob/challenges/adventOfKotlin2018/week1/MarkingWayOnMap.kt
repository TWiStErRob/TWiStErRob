package net.twisterrob.challenges.adventOfKotlin2018.week1

import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell
import java.lang.Math.sqrt
import java.util.PriorityQueue

fun addPath(inputMap: String): String {
	val map = Map.parse(inputMap)
	AStar(map).run()
	return map.toString()
}

class AStar(private val map: Map) {

	private val data: Array<Array<CellData>> =
		Array(map.rows) { row ->
			Array(map.cols) { col ->
				CellData(row, col, 0.0, 0.0, 0.0, null)
			}
		}

	fun run() {
		val openlist = PriorityQueue<CellData>(1, compareBy { it.totalCostEstimate })
		val target = map.endPosition
		openlist += map.startPosition
		map.startPosition.totalCostEstimate = 0.0
		val closedlist = mutableListOf<CellData>()
		while (openlist.isNotEmpty()) {
			val current = openlist.poll()!!
			if (current !== target) {
				closedlist += current
				for (neighbor in neighbours(current)) {
					if (neighbor in closedlist) continue
					val isOpen = openlist.remove(neighbor)
					if (isOpen) {
						val newD = current.distanceFromStart + current.distanceTo(neighbor)
						val newH = neighbor.heuristicTo(target)
						val newG = newD + newH
						if (newG < neighbor.totalCostEstimate) {
							neighbor.distanceFromStart = newD
							neighbor.heuristicToEnd = newH
							neighbor.totalCostEstimate = newG
							neighbor.parent = current
						}
					} else {
						neighbor.distanceFromStart = current.distanceFromStart + current.distanceTo(neighbor)
						neighbor.heuristicToEnd = neighbor.heuristicTo(target)
						neighbor.totalCostEstimate = neighbor.distanceFromStart + neighbor.heuristicToEnd
						neighbor.parent = current
					}
					openlist += neighbor
				}
			} else {
				println("Finished")
				var current: CellData? = target
				while (current != null) {
					map[current.row, current.col] = Cell.Path
					current = current.parent
				}
				return
			}
		}
	}

	override fun toString(): String {
		return data.joinToString("\n") { row ->
			row.joinToString("; ") {
				"(%2d,%2d^%2d,%2d)[%2.1f-%2.1f]".format(
					it.row, it.col,
					it.parent?.row ?: -1, it.parent?.col ?: -1,
					it.distanceFromStart, it.heuristicToEnd
				)
			}
		}
	}

	private fun neighbours(current: CellData): Sequence<CellData> {
		val directions = listOf(
			-1 to -1, -1 to 0, -1 to +1,
			0 to -1, /*self,*/ 0 to +1,
			+1 to -1, +1 to 0, +1 to +1
		)
		return directions
			.asSequence()
			.map { (rowOffset, colOffset) -> current.row + rowOffset to current.col + colOffset }
			.filter { (row, col) -> row in data.indices && col in data[row].indices }
			.map { (row, col) -> data[row][col] }
	}

	private fun Int.squared() = (this * this).toDouble()
	private fun CellData.heuristicTo(other: CellData): Double =
		sqrt((this.row - other.row).squared() + (this.col - other.col).squared())

	private fun CellData.distanceTo(other: CellData): Double {
		if (map[other.row, other.col] == Cell.Obstacle) {
			return Double.POSITIVE_INFINITY
		}
		val distances = arrayOf(
			arrayOf(1.5, 1.0, 1.5),
			arrayOf(1.0, 0.0, 1.0),
			arrayOf(1.5, 1.0, 1.5)
		)
		val rowD = this.row - other.row
		val colD = this.col - other.col
		require(rowD in -1..1)
		require(colD in -1..1)
		require(!(rowD == 0 && colD == 0))
		return distances[1 - rowD][1 - colD]
	}

	private val Map.startPosition get() = findPositionOf(Cell.Start)
	private val Map.endPosition get() = findPositionOf(Cell.End)

	private fun findPositionOf(cell: Cell): CellData {
		var found: CellData? = null
		data.forEachIndexed { rowIndex, row ->
			row.forEachIndexed { colIndex, current ->
				val mapCell = map[rowIndex, colIndex]
				if (mapCell == cell) {
					check(found == null) { "Multiple positions contain $cell: $found and $current" }
					found = current
				}
			}
		}
		return found ?: error("Cannot find $cell")
	}

	private data class CellData(
		val row: Int, val col: Int,
		var totalCostEstimate: Double, var distanceFromStart: Double, var heuristicToEnd: Double,
		var parent: CellData?
	)
}
