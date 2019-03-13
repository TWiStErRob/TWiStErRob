package net.twisterrob.test.jfixture

import com.flextrade.jfixture.JFixture
import com.flextrade.jfixture.utility.SpecimenType

/*
// String fixtString = fixture.create(String.class);
fun <T> JFixture.create(clazz: Class<T>): T = create(clazz)
// val fixtString: String = fixture.create(String::class.java)

inline fun <reified T> JFixture.create(): T = create(T::class.java)
// val fixtString: String = fixture.create<String>()
// val fixtString/*: String*/ = fixture.create<String>()
// val fixtString: String = fixture.create()

inline fun <reified T> JFixture.invoke(): T = create(T::class.java)
// val fixtString: String = fixture.invoke()

inline operator fun <reified T> JFixture.invoke(): T = create(T::class.java)
// val fixtString: String = fixture()
*/
inline operator fun <reified T> JFixture.invoke(): T =
	when {
		Collection::class.java.isAssignableFrom(T::class.java) ->
			create(object : SpecimenType<T>() {})
		else ->
			create(T::class.java)
	}

@Suppress("UNCHECKED_CAST") // can't have List<T>::class literal, so need to cast
inline fun <reified T : Any> JFixture.createList(size: Int = 3): List<T> =
	this.collections().createCollection(List::class.java as Class<List<T>>, T::class.java, size)

inline fun <reified E : Enum<E>> Enum.Companion.valuesExcluding(vararg excluded: E): Array<E> =
	(enumValues<E>().toList() - excluded).toTypedArray()

fun Any.setField(name: String, value: Any?) {
	this::class.java.getDeclaredField(name).apply {
		isAccessible = true
		set(this@setField, value)
	}
}
