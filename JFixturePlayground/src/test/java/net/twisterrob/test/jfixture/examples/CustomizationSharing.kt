package net.twisterrob.test.jfixture.examples

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.invoke
import org.hamcrest.Matchers.emptyString
import org.hamcrest.Matchers.not
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.Random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import org.junit.jupiter.api.TestMethodOrder

private val fixture = JFixture()

@TestMethodOrder(Random::class)
class SharedStateTest {

	@Test fun customizer() {
		fixture.customise().sameInstance(String::class.java, "")

		assertThat(fixture<String>(), emptyString())
	}

	@Test fun `annoyed observer`() {
		assertThat(fixture<String>(), not(emptyString()))
	}
}

@TestMethodOrder(Random::class)
@TestInstance(PER_CLASS) // could be configured globally as well
class AlmostNonSharedStateTest {

	private val fixture = JFixture()

	@Test fun customizer() {
		fixture.customise().sameInstance(String::class.java, "")

		assertThat(fixture<String>(), emptyString())
	}

	@Test fun `annoyed observer`() {
		assertThat(fixture<String>(), not(emptyString()))
	}
}

@TestMethodOrder(Random::class)
@TestInstance(PER_METHOD) // default
class NonSharedStateTest {

	private lateinit var fixture: JFixture

	@BeforeEach fun setUp() {
		fixture = JFixture()
	}

	@Test fun customizer() {
		fixture.customise().sameInstance(String::class.java, "")

		assertThat(fixture<String>(), emptyString())
	}

	@Test fun `annoyed observer`() {
		assertThat(fixture<String>(), not(emptyString()))
	}
}
class MyFixture : JFixture() {
	init {
		customise().useSubType(CharSequence::class.java, String::class.java)
	}
}
