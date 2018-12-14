package net.twisterrob.challenges.adventOfKotlin2018.week3

//interface SortedMutableList<T> : Iterable<T> {
//	val size: Int
//	fun add(element: T)
//	fun remove(element: T)
//	operator fun get(index: Int): T
//	operator fun contains(element: T): Boolean
//}

typealias SortedMutableList<T> = java.util.PriorityQueue<T>

operator fun <T : Comparable<T>> SortedMutableList<T>.get(index: Int): T =
	asSequence().sortedWith(comparator() ?: naturalOrder<T>()).elementAt(index)

fun <T : Comparable<T>> sortedMutableListOf(vararg elements: T): SortedMutableList<T> {
	return SortedMutableList(elements.asList())
}

fun <T> sortedMutableListOf(comparator: Comparator<T>, vararg elements: T): SortedMutableList<T> {
	return SortedMutableList(elements.size + 1, comparator).apply { addAll(elements) }
}
