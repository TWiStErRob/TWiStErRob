package net.twisterrob.test.jfixture.features

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
