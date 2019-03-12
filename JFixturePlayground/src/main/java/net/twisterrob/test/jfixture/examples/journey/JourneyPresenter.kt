package net.twisterrob.test.jfixture.examples.journey

import java.time.Duration

interface JourneyView {
	fun show(model: Model)
}

data class Model(
	val journeyId: String,
	val origin: String,
	val destination: String,
	val length: Duration,
	val changeCount: Int,
	val trainOnly: Boolean
)

interface DataSource<T> {
	fun getById(id: String): Single<T>
}

class JourneyPresenter(
	private val view: JourneyView,
	private val dataSource: DataSource<Journey>,
	private val mapper: (Journey) -> Model
) {

	fun load(journeyId: String) {
		dataSource
			.getById(journeyId)
			.map(mapper)
			.subscribe { model ->
				view.show(model)
			}
	}
}

class Single<T> private constructor(private val value: T) {

	fun <R> map(mapper: (T) -> R): Single<R> =
		Single(mapper(value))

	fun subscribe(observer: (T) -> Unit) = observer(value)

	companion object {
		fun <T> just(value: T) = Single(value)
	}
}
