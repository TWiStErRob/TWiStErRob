package net.twisterrob.challenges.adventOfKotlin2018.week1

class Map(private val cells: Array<Array<Cell>>) {

	val rows: Int = cells.rows
	val cols: Int = cells.cols
	override fun toString() =
		cells.joinToString("\n") { row ->
			row.joinToString(separator = "", transform = Cell::format)
		}

	operator fun get(row: Int, col: Int) = cells[row][col]
	operator fun set(row: Int, col: Int, value: Cell) {
		cells[row][col] = value
	}

	enum class Cell(private val display: Char) {
		Empty('.'),
		Obstacle('B'),
		Start('S'),
		End('X'),
		Path('*');

		fun format() = display.toString()

		companion object {
			fun parse(cell: Char) = enumValues<Cell>().find { it.display == cell }
				?: error("Unknown cell: ${cell}")
		}
	}

	companion object {

		fun parse(inputMap: String): Map {
			val rows = inputMap.split('\n')
			val rowCount = rows.size
			check(rowCount != 0) {
				"No rows in input map"
			}
			val columnCount = rows[0].length
			check(rows.all { it.length == columnCount }) {
				"Map not rectangular: not all rows have the same column count: " + rows.filter { it.length != columnCount }
			}

			fun parseRow(row: String) = row.map(Map.Cell.Companion::parse).toTypedArray()
			return Map(cells = rows.map(::parseRow).toTypedArray())
		}

		private val Array<Array<Cell>>.rows: Int get() = this.size
		private val Array<Array<Cell>>.cols: Int get() = if (rows != 0) this[0].size else 0
	}
}
