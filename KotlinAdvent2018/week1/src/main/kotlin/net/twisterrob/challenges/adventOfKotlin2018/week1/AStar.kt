package net.twisterrob.challenges.adventOfKotlin2018.week1

import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell
import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell.End
import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell.Path
import net.twisterrob.challenges.adventOfKotlin2018.week1.Map.Cell.Start
import java.util.PriorityQueue

class AStar(private val map: Map) {

	data class Pos(
		val row: Int,
		val col: Int
	)

	private class CellData(
		val pos: Pos,
		var totalCostEstimate: Double = 0.0,
		var distanceFromStart: Double = 0.0,
		var heuristicToEnd: Double = 0.0,
		var parent: CellData? = null
	) {

		override fun toString() =
			"(%2d,%2d^%2d,%2d)[%2.1f-%2.1f]".format(
				pos.row, pos.col,
				parent?.pos?.row ?: -1, parent?.pos?.col ?: -1,
				distanceFromStart, heuristicToEnd
			)
	}

	private val data: Array<Array<CellData>> =
		Array(map.rows) { row ->
			Array(map.cols) { col ->
				CellData(Pos(row, col))
			}
		}

	override fun toString() =
		data.joinToString("\n") { row ->
			row.joinToString("; ")
		}

	/**
	 * @param heuristic between any two valid positions on the map
	 * @param distance distance will be always between two neighboring cells
	 */
	fun run(heuristic: (a: Pos, b: Pos) -> Double, distance: Map.(a: Pos, b: Pos) -> Double) {
		val openCells = PriorityQueue<CellData>(compareBy(CellData::totalCostEstimate))
		openCells += findPositionOf(Start)
		openCells.peek().totalCostEstimate = 0.0
		val closedCells = mutableSetOf<CellData>()
		val target = findPositionOf(End)
		while (openCells.isNotEmpty()) {
			val current = openCells.poll()!!
			if (current !== target) {
				closedCells += current
				for (neighbor in current.neighbors()) {
					if (neighbor in closedCells) continue
					// performance and correctness: need to remove neighbor from openCells,
					val isOpen = openCells.remove(neighbor)
					// because we're mutating it, will be re-added after mutation
					neighbor.update(current, target, isOpen, heuristic, distance)
					// (re)adding simulates a heapify to re-sort priority queue
					// (complexity of algorithm stays the same, except the heap operation will have a 2x)
					openCells += neighbor
				}
			} else {
				target.backtrackPathToStart().forEach { map.mark(it.pos) }
				return
			}
		}
	}

	private fun CellData.update(
		current: CellData,
		target: CellData,
		neighborIsOpen: Boolean,
		heuristic: (a: Pos, b: Pos) -> Double,
		distance: Map.(a: Pos, b: Pos) -> Double
	) {
		val newDistance = current.distanceFromStart + map.distance(current.pos, this.pos)
		if (neighborIsOpen) {
			if (newDistance < distanceFromStart) {
				// better total cost than before, use this new path (parent)
				distanceFromStart = newDistance
				totalCostEstimate = distanceFromStart + heuristicToEnd
				parent = current
			} else {
				// neighbor's current reachability is better than this one
			}
		} else {
			// reaching this cell for the first time, calculate everything
			distanceFromStart = newDistance
			heuristicToEnd = heuristic(this.pos, target.pos)
			totalCostEstimate = distanceFromStart + heuristicToEnd
			parent = current
		}
	}

	private fun CellData.backtrackPathToStart() = generateSequence(this) { it.parent }

	private fun CellData.neighbors(): Sequence<CellData> {
		operator fun Pos.plus(offset: Offset) = Pos(this.row + offset.row, this.col + offset.col)

		return NEIGHBOR_DIRECTIONS
			.asSequence()
			.map { offset -> pos + offset }
			.filter { pos -> data.isValidPosition(pos) }
			.map { (row, col) -> data[row][col] }
	}

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

	companion object {
		private data class Offset(val row: Int, val col: Int)

		private val NEIGHBOR_DIRECTIONS = listOf(
			Offset(-1, -1), Offset(-1, 0), Offset(-1, +1),
			Offset(0, -1), /* self not included, */ Offset(0, +1),
			Offset(+1, -1), Offset(+1, 0), Offset(+1, +1)
		)

		private fun Array<Array<CellData>>.isValidPosition(pos: Pos) =
			pos.row in this.indices && pos.col in this[pos.row].indices

		private fun Map.mark(pos: Pos) {
			this[pos.row, pos.col] = Path
		}
	}
}
