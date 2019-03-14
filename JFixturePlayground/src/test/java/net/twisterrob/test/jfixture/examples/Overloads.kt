package net.twisterrob.test.jfixture.examples

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.invoke
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class Overloads {
	@Test fun `wrong overload leads to crash`() {
		val fixture = JFixture()
		fixture.customise().sameInstance(CharSequence::class.java, String::class.java)
		fixture.apply {
			customise().useSubType(CharSequence::class.java, String::class.java)
		}
		val data: CharSequence = fixture()

		assertThat(data, instanceOf(String::class.java))
	}

	@Test fun `wrong overload leads to crash, fixed`() {
		val fixture = JFixture()
		fixture.customise().useSubType(CharSequence::class.java, String::class.java)

		val data: CharSequence = fixture()

		assertThat(data, instanceOf(String::class.java))
	}
}
