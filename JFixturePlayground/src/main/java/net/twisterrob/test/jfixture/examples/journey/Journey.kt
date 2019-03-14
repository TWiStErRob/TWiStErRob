package net.twisterrob.test.jfixture.examples.journey

import java.time.LocalDateTime

data class Journey(
	val id: String,
	val legs: List<Leg>,
	val passengers: List<Passenger>
) {

	val changeCount get () = legs.size - 1
}

data class Leg(
	val origin: Stop,
	val departure: LocalDateTime,
	val mode: TransportMode,
	val destination: Stop,
	val arrival: LocalDateTime
)

data class Stop(
	val id: String,
	val name: String
)

data class Passenger(
	val name: String,
	val preferredMode: TransportMode
)

enum class TransportMode {
	WALK,
	TRAIN,
	TAXI
}
