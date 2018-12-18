package net.twisterrob.challenges.adventOfKotlin2018.week4

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

typealias KInvocationHandler = (proxy: Any, method: Method, args: Array<Any?>) -> Any?

/**
 * Kotlin convenience for calling [Proxy.newProxyInstance].
 * @param handler `args` will be non-null
 */
fun newProxyInstance(
	vararg interfaces: KClass<*>,
	classLoader: ClassLoader = interfaces[0].java.classLoader,
	handler: KInvocationHandler
): Any {
	val javaInterfaces = interfaces.map { it.java }.toTypedArray()

	fun handlerDelegate(proxy: Any, method: Method, args: Array<Any?>?) =
		handler(proxy, method, args ?: emptyArray())

	return Proxy.newProxyInstance(classLoader, javaInterfaces, ::handlerDelegate)
}

internal fun Class<*>.defaultValueForReflection(): Any? {
	@Suppress("RemoveRedundantCallsOfConversionMethods")
	return when (this) {
		Byte::class.javaPrimitiveType -> 0.toByte()
		Char::class.javaPrimitiveType -> 0.toChar()
		Short::class.javaPrimitiveType -> 0.toShort()
		Int::class.javaPrimitiveType -> 0.toInt()
		Long::class.javaPrimitiveType -> 0.toLong()
		Float::class.javaPrimitiveType -> 0.toFloat()
		Double::class.javaPrimitiveType -> 0.toDouble()
		Boolean::class.javaPrimitiveType -> false
		else -> null
	}
}
