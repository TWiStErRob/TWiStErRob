package net.twisterrob.challenges.adventOfKotlin2018.week2

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Twinject {

	inline operator fun invoke(block: Twinject.() -> Unit): Twinject = this.apply(block)

	private val locator = mutableMapOf<KClass<*>, () -> Any>()

	inline fun <reified T : Any> register(noinline provider: () -> T) = register(T::class, provider)

	fun <T : Any> register(type: KClass<T>, provider: () -> T) {
		locator[type] = provider
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <T : Any> get(type: KClass<T>): T =
		locator.getValue(type)() as T

	inline fun <reified T : Any> inject(): T = get(T::class)

	inline operator fun <reified T : Any> invoke(): T = inject()

	inline operator fun <reified T : Any> getValue(receiver: Any, property: KProperty<*>): T = inject()
}

inline fun <reified T : Any> singleton(noinline provider: () -> T): () -> T {
	val lazy = lazy { provider() }
	return { lazy.value }
}
