package net.twisterrob.test.jfixture.examples

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.invoke
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class PrimaryConstructor {


	@Test fun `read-only property initialized`() {
		class Data(val prop: String)

		val fixture = JFixture()

		val data = fixture<Data>()

		assertThat(data.prop, notNullValue())
	}

	@Test fun `propertyOf doesn't work on primary constructor property`() {
		class Data(var prop: String)

		val fixture = JFixture()
		fixture.customise().propertyOf(Data::class.java, "prop", "fix")

		val data = fixture<Data>()

		assertThat(data.prop, equalTo("fix"))
	}

	@Test fun `propertyOf works on mutable property`() {
		class Data {
			var prop: String = ""
		}

		val fixture = JFixture()
		fixture.customise().propertyOf(Data::class.java, "prop", "fix")

		val data = fixture<Data>()

		assertThat(data.prop, equalTo("fix"))
	}
}
