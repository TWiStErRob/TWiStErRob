package net.twisterrob.test.jfixture.examples.journey

import com.flextrade.jfixture.JFixture
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import net.twisterrob.test.jfixture.invoke
import org.junit.jupiter.api.Test

class JourneyPresenterTest {
	private val mockView: JourneyView = mock()
	private val mockDataSource: DataSource<Journey> = mock()
	private val mockMapper: (Journey) -> Model = mock()

	private val sut = JourneyPresenter(mockView, mockDataSource, mockMapper)

	private val fixture = JFixture()
	private val fixtJourneyId: String = fixture()
	private val fixtJourney: Journey = fixture()
	private val fixtModel: Model = fixture()

	@Test fun `loads journey and presents it to view`() {
		whenever(mockDataSource.getById(fixtJourneyId)).thenReturn(Single.just(fixtJourney))
		whenever(mockMapper(fixtJourney)).thenReturn(fixtModel)

		sut.load(fixtJourneyId)

		verify(mockView).show(fixtModel)
	}
}
