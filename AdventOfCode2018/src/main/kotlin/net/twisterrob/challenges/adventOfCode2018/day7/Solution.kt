package net.twisterrob.challenges.adventOfCode2018.day7

import java.io.File

fun main(vararg args: String) {
    val file = args.first()
    val fileContents = File(file).readText()
    solve(fileContents)
}

fun solve(input: String): String {
    val linePattern = Regex("""^Step ([A-Z]) must be finished before step ([A-Z]) can begin.$""")
    fun MatchResult.groupAsStep(group: Int) = this.groups[group]!!.value[0]
    fun parseLine(line: String): Pair<Char, Char> =
        linePattern.matchEntire(line)!!.let { match ->
            match.groupAsStep(1) to match.groupAsStep(2)
        }

    val constraints = input
        .lines()
        .filter(String::isNotEmpty)
        .map(::parseLine)

    return solve(*constraints.toTypedArray())
}

fun solve(vararg partConstraints: Pair<Char, Char>): String =
    findOrder(*partConstraints).joinToString(separator = "")

/**
 * @see [https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm]
 */
private fun findOrder(vararg edges: Pair<Char, Char>): List<Char> {

    class Graph(vararg edges: Pair<Char, Char>) {
        private val graph = mutableSetOf(*edges)

        operator fun minusAssign(edge: Pair<Char, Char>) {
            graph -= edge
        }

        fun hasIncomingEdges(node: Char) = graph.any { it.second == node }

        // this blows up the O complexity, but the solvable input is small
        fun dependentsFrom(node: Char) = graph.filter { it.first == node }

        fun hasAnyEdges() = graph.isNotEmpty()
    }

    val graph = Graph(*edges)
    val allNodes = edges.flatMap { listOf(it.first, it.second) }.toSet()
    // SortedSet to satisfy "If more than one step is ready, choose the step which is first alphabetically."
    val possibleNextNodes = allNodes.filterNot(graph::hasIncomingEdges).toSortedSet()

    val result = mutableListOf<Char>()
    while (possibleNextNodes.isNotEmpty()) {
        val node = possibleNextNodes.first()
        possibleNextNodes -= node
        result += node
        val nextEdges = graph.dependentsFrom(node)
        nextEdges.forEach { edge ->
            graph -= edge
            if (!graph.hasIncomingEdges(edge.second)) {
                possibleNextNodes += edge.second
            }
        }
    }
    if (graph.hasAnyEdges()) {
        error("cycle")
    } else {
        return result
    }
}
