package net.twisterrob.challenges.adventOfKotlin2018.week3

interface SortedMutableList<T> : Iterable<T> {
	val size: Int
	fun add(element: T)
	fun remove(element: T)
	operator fun get(index: Int): T
	operator fun contains(element: T): Boolean
}

fun <T : Comparable<T>> sortedMutableListOf(vararg elements: T): SortedMutableList<T> {
	TODO()
}

fun <T> sortedMutableListOf(comparator: Comparator<T>, vararg elements: T): SortedMutableList<T> {
	TODO()
}
