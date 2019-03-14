package net.twisterrob.test.jfixture.examples

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.examples.FlakyTest.Type.Type0
import net.twisterrob.test.jfixture.invoke
import net.twisterrob.test.jfixture.valuesExcluding
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class FlakyTest {

	enum class Type {
		Type0,
		Type1,
		Type2,
		Type3,
		Type4,
		Type5,
		Type6,
		Type7,
		Type8,
		Type9,
	}

	@Test fun flaky1() {
		val fixture = JFixture()

		val type: Type = fixture()

		assertNotEquals(Type0, type)
	}

	@Test fun flaky1_fixed() {
		val fixture = JFixture()
		fixture.customise().lazyInstance(Type::class.java) {
			fixture.create().fromList(*Enum.valuesExcluding(Type0))
		}

		val type: Type = fixture()

		assertNotEquals(Type0, type)
	}
}
