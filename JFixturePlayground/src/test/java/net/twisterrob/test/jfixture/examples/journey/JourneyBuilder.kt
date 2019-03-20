package net.twisterrob.test.jfixture.examples.journey

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset

class JourneyBuilder(
	private var id: String = "",
	private val legs: MutableList<Leg> = mutableListOf()
) {

	fun build() = Journey(id, legs, emptyList())

	fun setId(id: String): JourneyBuilder = this.apply { this.id = id }
	fun addLeg(leg: Leg): JourneyBuilder = this.apply { legs.add(leg) }
}

class LegBuilder(
	private var origin: Stop = Stop("", ""),
	private var departure: LocalDateTime = LocalDateTime.now(clock),
	private var mode: TransportMode = TransportMode.WALK,
	private var destination: Stop = Stop("", ""),
	private var arrival: LocalDateTime = LocalDateTime.now(clock)
) {

	fun build() = Leg(origin, departure, mode, destination, arrival)

	fun setOrigin(origin: Stop): LegBuilder = this.apply { this.origin = origin }
	fun setMode(mode: TransportMode): LegBuilder = this.apply { this.mode = mode }
	// ...

	companion object {
		private val clock = Clock.tickMillis(ZoneOffset.UTC)
	}
}
