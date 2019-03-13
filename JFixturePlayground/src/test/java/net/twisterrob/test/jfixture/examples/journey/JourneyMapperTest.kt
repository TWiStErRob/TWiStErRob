package net.twisterrob.test.jfixture.examples.journey

import com.flextrade.jfixture.JFixture
import net.twisterrob.test.jfixture.createList
import net.twisterrob.test.jfixture.examples.journey.TransportMode.TRAIN
import net.twisterrob.test.jfixture.invoke
import net.twisterrob.test.jfixture.setField
import net.twisterrob.test.jfixture.valuesExcluding
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER
import org.junit.jupiter.params.provider.CsvSource
import java.time.Duration
import java.time.temporal.ChronoUnit

class JourneyMapperTest {

	private val sut = JourneyMapper()
	private val fixture = JFixture()

	@CsvSource("1, 0", "2, 1", "3, 2", "4, 3")
	@ParameterizedTest(name = "changeCount[$INDEX_PLACEHOLDER]: when {0} legs, expect {1} changes")
	fun `changeCount is mapped correctly`(legCount: Int, expectedChanges: Int) {
		fixture.customise().repeatCount(legCount)
		val journey: Journey = fixture()

		val result = sut.invoke(journey)

		assertThat(result.changeCount, equalTo(expectedChanges))
	}

	@Test fun `trainOnly is true for a TRAIN-only journey`() {
		fixture.customise().sameInstance(TransportMode::class.java, TRAIN)
		val journey: Journey = fixture()

		val result = sut.invoke(journey)

		assertThat(result.trainOnly, equalTo(true))
	}

	@Test fun `trainOnly is false for a TRAIN-less journey`() {
		fixture.customise().lazyInstance(TransportMode::class.java) {
			fixture.create().fromList(*Enum.valuesExcluding(TRAIN))
		}
		val journey: Journey = fixture()

		val result = sut.invoke(journey)

		assertThat(result.trainOnly, equalTo(false))
	}

	@Test fun `trainOnly is true for a TRAIN-only journey v2`() {
		fixture.customise().propertyOf(Leg::class.java, "mode", TRAIN)
		val journey: Journey = fixture()

		val result = sut.invoke(journey)

		assertThat(result.trainOnly, equalTo(true))
	}

	@Test fun `trainOnly is true for a TRAIN-only journey v3`() {
		fixture.customise().intercept(Leg::class.java) {
			it.setField("mode", TRAIN)
		}
		val journey: Journey = fixture()

		val result = sut.invoke(journey)

		assertThat(result.trainOnly, equalTo(true))
	}

	@Test fun `duration returns time for whole journey`() {
		val journey: Journey = fixture()
		journey.legs.last().setField("arrival", journey.legs.first().departure.plusMinutes(15))

		val result = sut.invoke(journey)

		assertThat(result.length, equalTo(Duration.of(15, ChronoUnit.MINUTES)))
	}

	@Test fun `2 passengers 1 leg`() {
		val journey: Journey = fixture()
		journey.setField("legs", fixture.createList<Leg>(2))
		journey.setField("passengers", fixture.createList<Passenger>(1))
	}
}
