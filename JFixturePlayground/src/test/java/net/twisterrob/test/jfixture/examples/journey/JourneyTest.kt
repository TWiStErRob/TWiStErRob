package net.twisterrob.test.jfixture.examples.journey

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.features.invoke
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class JourneyTest {

	@RepeatedTest(100) fun `two journeys are better than one`() {
		val twoJourneys = setOf(journey().addLeg().finish().build(), journey().addLeg().finish().build())
		assertThat(twoJourneys, hasSize(2))
	}

	@Test fun `two journeys are better than one with JFixture`() {
		val fixture = JFixture()
		val twoJourneys: Set<Journey> = setOf(fixture(), fixture())
		assertThat(twoJourneys, hasSize(2))
	}
}
