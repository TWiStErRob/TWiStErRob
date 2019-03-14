package net.twisterrob.test.jfixture.examples.journey

import com.flextrade.jfixture.FixtureAnnotations
import com.flextrade.jfixture.JFixture
import com.flextrade.jfixture.annotations.Fixture
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import net.twisterrob.test.jfixture.invoke
import org.hamcrest.Matchers.equalTo
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JourneyPresenterTest {
	@Mock lateinit var mockView: JourneyView
	@Mock lateinit var mockDataSource: DataSource<Journey>
	@Mock lateinit var mockMapper: (Journey) -> Model

	@Fixture lateinit var fixtJourneyId: String
	@Fixture lateinit var fixtJourney: Journey
	@Fixture lateinit var fixtModel: Model

	private lateinit var fixture: JFixture

	private lateinit var sut: JourneyPresenter

	@BeforeEach fun setUp() {
		fixture = JFixture()
		FixtureAnnotations.initFixtures(this, fixture)
		MockitoAnnotations.initMocks(this)

		sut = JourneyPresenter(mockView, mockDataSource, mockMapper)
	}

	@Test fun `loads journey and presents it to view`() {
		whenever(mockDataSource.getById(fixtJourneyId)).thenReturn(Single.just(fixtJourney))
		whenever(mockMapper(fixtJourney)).thenReturn(fixtModel)

		sut.load(fixtJourneyId)

		verify(mockView).show(fixtModel)
	}

	@Test fun spied() {
		val journey = spy(fixture<Journey>())
		doReturn(6).whenever(journey).changeCount

		// later in production code
		val changes = journey.changeCount

		assertThat(changes, equalTo(6)) // without spy this would be 2
	}
}
