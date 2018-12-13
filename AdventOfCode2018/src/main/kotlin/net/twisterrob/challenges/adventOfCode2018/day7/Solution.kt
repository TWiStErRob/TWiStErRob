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
    val graph = mutableSetOf(*edges)
    val L = mutableListOf<Char>()
    val S = graph
        .flatMap { listOf(it.first, it.second) }
        .filterNot { node -> graph.any { it.second == node } }
        .toSortedSet()
    while (S.isNotEmpty()) {
        val node = S.first()
        S -= node
        L += node
        val nextEdge = graph.filter { it.first == node }
        nextEdge.forEach { (n, m) ->
            graph -= n to m
            if (graph.count { it.second == m } == 0) {
                S += m
            }
        }
    }
    if (graph.isNotEmpty()) {
        error("cycle")
    } else {
        return L
    }
}
