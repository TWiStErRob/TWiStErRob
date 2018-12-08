package net.twisterrob.challenges.adventOfKotlin2018.week2

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Twinject {

	data class Type(
		private val type: KClass<*>,
		private val nullable: Boolean
	)

	inline operator fun invoke(block: Twinject.() -> Unit): Twinject = this.apply(block)

	private val locator = mutableMapOf<Type, () -> Any?>()

	inline fun <reified T : Any> register(noinline provider: () -> T) = register(T::class, provider)

	fun <T : Any> register(type: KClass<T>, provider: () -> T) {
		locator[Type(type, false)] = provider
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <T : Any> get(type: KClass<T>): T =
		locator.getValue(Type(type, false))() as T

	inline fun <reified T : Any> inject(): T = get(T::class)

	inline operator fun <reified T : Any> invoke(): T = inject()

	inline operator fun <reified T : Any> getValue(receiver: Any, property: KProperty<*>): T = inject()
}

inline fun <reified T : Any> Twinject.registerSingleton(noinline provider: () -> T) {
	val lazy = lazy { provider() }
	register { lazy.value }
}
