package net.twisterrob.test.jfixture.examples.journey

import net.twisterrob.test.jfixture.examples.journey.TransportMode.TRAIN
import java.time.Duration

class JourneyMapper : (Journey) -> Model {
	override fun invoke(journey: Journey) = Model(
		journey.id,
		origin = journey.legs.first().origin.name,
		destination = journey.legs.last().destination.name,
		length = Duration.between(journey.legs.first().departure, journey.legs.last().arrival),
		changeCount = journey.legs.size - 1,
		trainOnly = journey.legs.all { it.mode == TRAIN }
	)
}
