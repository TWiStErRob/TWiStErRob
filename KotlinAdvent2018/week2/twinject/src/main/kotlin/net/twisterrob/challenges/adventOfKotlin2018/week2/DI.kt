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

	inline fun <reified T : Any> register(noinline provider: () -> T) =
		register(T::class, provider)

	inline fun <reified T : Any> registerNullable(noinline provider: () -> T?) =
		registerNullable(T::class, provider)

	fun <T : Any> register(type: KClass<T>, provider: () -> T) {
		locator[Type(type, false)] = provider
	}

	fun <T : Any> registerNullable(type: KClass<T>, provider: () -> T?) {
		locator[Type(type, true)] = provider
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <T : Any> get(type: KClass<T>): T =
		locator.getValue(Type(type, false))() as T

	@Suppress("UNCHECKED_CAST")
	fun <T : Any> getNullable(type: KClass<T>): T? =
		locator.getValue(Type(type, true))() as T?

	inline fun <reified T : Any> inject(): T =
		get(T::class)

	@Suppress("UNCHECKED_CAST")
	// can't pass otherwise: T::class is KClass<T?> even though that's not possible
	inline fun <reified T : Any?> injectNullable(): T? =
		getNullable(T::class as KClass<Any>) as T?

	inline operator fun <reified T : Any> invoke(): T = inject()

	inline operator fun <reified T : Any> getValue(receiver: Any, property: KProperty<*>): T = inject()
}

inline fun <reified T : Any> singleton(noinline provider: () -> T): () -> T {
	val lazy = lazy { provider() }
	return { lazy.value }
}
