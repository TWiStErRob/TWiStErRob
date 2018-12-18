package net.twisterrob.challenges.adventOfKotlin2018.week4

import java.lang.reflect.Method

inline fun <reified T> mock(): T =
	newProxyInstance(T::class, handler = MockInvocationHandler()) as T

fun <T> setReturnValue(call: () -> T, value: T): Unit {
	call()
}

fun <T> setBody(call: () -> T, behavior: () -> T): Unit {
	call()
}

class MockInvocationHandler : KInvocationHandler {
	override fun invoke(proxy: Any, method: Method, args: Array<Any?>): Any? {
		val me = "${MockInvocationHandler::class.java.simpleName}+${System.identityHashCode(proxy).toString(16)}"
		println("$me.${method.name}(${args.joinToString()})")
		return when {
			method.declaringClass == Object::class.java ->
				when (method) {
					Object::class.java.getDeclaredMethod("toString") ->
						"${proxy::class.java}@${System.identityHashCode(proxy).toString(16)}"
					Object::class.java.getDeclaredMethod("hashCode") ->
						System.identityHashCode(proxy)
					Object::class.java.getDeclaredMethod("equals", Object::class.java) ->
						proxy === args[0]
					else -> error("Unhandled Object method: $method")
				}
			else ->
				method.returnType.defaultValueForReflection()
		}
	}
}
