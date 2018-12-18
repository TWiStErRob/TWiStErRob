package net.twisterrob.challenges.adventOfKotlin2018.week4

import java.lang.reflect.Method

private val NOT_RECORDING = fun() { error("Should never be called") }
private var behaviorToRecord: () -> Any? = NOT_RECORDING

inline fun <reified T> mock(): T =
	newProxyInstance(T::class, handler = MockInvocationHandler()) as T

fun <T> setReturnValue(call: () -> T, value: T) =
	setBody(call, { value })

fun <T> setBody(call: () -> T, behavior: () -> T) {
	check(behaviorToRecord == NOT_RECORDING) {
		"Already recording, make sure you don't call setReturnValue and setBody nested or in one another"
	}
	try {
		behaviorToRecord = behavior
		call()
		check(behaviorToRecord == NOT_RECORDING) {
			"No stubbing call was issued inside setReturnValue or setBody"
		}
	} finally {
		behaviorToRecord = NOT_RECORDING
	}
}

internal class Stub(
	private val method: Method,
	private val args: Array<Any?>,
	private val response: () -> Any?
) {

	operator fun invoke(): Any? = response()

	override fun toString() =
		"${method.name}(${args.joinToString()}) = ${response}"

	fun matches(method: Method, args: Array<Any?>): Boolean =
		this.method == method && this.args.asList() == args.asList()
}

class MockInvocationHandler : KInvocationHandler {

	private lateinit var proxy: Any
	private val stubs: MutableList<Stub> = mutableListOf()

	override fun invoke(proxy: Any, method: Method, args: Array<Any?>): Any? {
		if (!this::proxy.isInitialized) {
			this.proxy = proxy
		}
		require(this.proxy === proxy) {
			"Each ${MockInvocationHandler::class} handles a single mock target only."
		}
		val call = "${method.name}(${args.joinToString()})"
		when (behaviorToRecord) {
			NOT_RECORDING -> when {
				method.declaringClass == Object::class.java -> {
					println("$this.$call")
					return invokeObjectMethod(method, proxy, args)
				}

				else -> {
					println("Responding for $this.$call")
					val matchingStubs = stubs.filter { it.matches(method, args) }
					when (matchingStubs.size) {
						0 -> error("No matching stub found for $call\n" + describe(method, args))
						1 -> return stubs.single().invoke()
						else -> error("Multiple matching stubs found for $call\n" + describe(method, args))
					}
				}
			}

			else -> {
				println("Recording $this.$call as $behaviorToRecord")
				stubs += Stub(method, args, behaviorToRecord)
				return method.returnType.defaultValueForReflection()
					.also { behaviorToRecord = NOT_RECORDING }
			}
		}
	}

	override fun toString(): String {
		val name = MockInvocationHandler::class.java.simpleName
		val hash = System.identityHashCode(proxy).toString(16)
		return "$name+$hash"
	}

	private fun describe(method: Method, args: Array<Any?>) = buildString {
		if (stubs.isEmpty()) {
			append("No stubs")
		} else {
			stubs.forEach { stub ->
				if (stub.matches(method, args)) {
					append("MATCHES ")
				}
				append(stub)
				append('\n')
			}
		}
	}

	private fun invokeObjectMethod(method: Method, proxy: Any, args: Array<Any?>): Any =
		when (method) {
			Object::class.java.getDeclaredMethod("toString") ->
				"${proxy::class.java.canonicalName}@${System.identityHashCode(proxy).toString(16)}"
			Object::class.java.getDeclaredMethod("hashCode") ->
				System.identityHashCode(proxy)
			Object::class.java.getDeclaredMethod("equals", Object::class.java) ->
				proxy === args[0]
			else -> error("Unhandled Object method: $method")
		}
}
