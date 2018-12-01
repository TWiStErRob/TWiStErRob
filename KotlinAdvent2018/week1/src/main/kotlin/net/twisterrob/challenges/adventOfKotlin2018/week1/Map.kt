package net.twisterrob.challenges.adventOfKotlin2018.week1

class Map(val cells: Array<Array<Cell>>) {

	override fun toString() =
		cells.joinToString("\n") { row ->
			row.joinToString(separator = "", transform = Cell::format)
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
	}
}
