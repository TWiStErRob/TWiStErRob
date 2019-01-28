package net.twisterrob.test.jfixture.features

import com.flextrade.jfixture.JFixture
import org.junit.jupiter.api.Test

class KotlinTest {

	@Suppress("UNUSED_VARIABLE")
	@Test fun test() {
		val fixture = JFixture()

		val fixtStringExplicit: String = fixture.create(String::class.java)
		val fixtStringInferred: String = fixture.invoke()
		val fixtStringShortest: String = fixture()
	}
}
