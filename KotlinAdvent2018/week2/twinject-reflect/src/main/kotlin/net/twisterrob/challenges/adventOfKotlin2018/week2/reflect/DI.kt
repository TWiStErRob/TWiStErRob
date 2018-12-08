package net.twisterrob.challenges.adventOfKotlin2018.week2.reflect

import net.twisterrob.challenges.adventOfKotlin2018.week2.Twinject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : Any> Twinject.registerSelf() {
	register(T::class)
}

inline fun <reified T : Any, reified T2 : T> Twinject.register() {
	register<T>(T2::class)
}

inline fun <reified T : Any> Twinject.register(impl: KClass<out T>) {
	register(T::class) { ReflectiveCreator(this, impl)() } // TODO ReflectiveCreator is not () -> T
}

class ReflectiveCreator<T : Any>(
	private val inject: Twinject,
	private val impl: KClass<T>
) {

	private val <T> KFunction<T>.isInjectable: Boolean
		get() = parameters.all { it.type.jvmErasure == Twinject::class || it.isOptional }

	operator fun invoke(): T {
		val ctor = impl.constructors.single { it.isInjectable }
		@Suppress("USELESS_CAST") // make type inference more general
		val args = ctor.parameters
			.map {
				it to when {
					it.type.jvmErasure == Twinject::class -> inject
					else -> null as Any?
				}
			}
			.filterNot { it.second == null }
			.toMap()
		return ctor.callBy(args)
	}
}
