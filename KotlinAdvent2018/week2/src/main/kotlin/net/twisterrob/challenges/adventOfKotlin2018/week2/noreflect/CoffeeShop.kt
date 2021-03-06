package net.twisterrob.challenges.adventOfKotlin2018.week2.noreflect

import net.twisterrob.challenges.adventOfKotlin2018.week2.Twinject

val inject = (Twinject()) {
	// reflective creation (same type)
	register { CoffeeMaker() }
	// reflective creation (super type)
	register<Pump> { Thermosiphon() }
	// custom creation
	register<Heater> { EletrictHeater() }
}

fun main(args: Array<String>) {
	val maker: CoffeeMaker = inject()
	maker.brew()
}

interface Pump {
	fun pump()
}

interface Heater {
	fun heat()
}

class Thermosiphon(
	private val heater: Heater = inject()
) : Pump {

	override fun pump() {
		heater.heat()
		println("=> => pumping => =>")
	}
}

class CoffeeMaker {

	private val pump: Pump by inject

	fun brew() {
		pump.pump()
		println(" [_]P coffee! [_]P")
	}
}

class EletrictHeater : Heater {
	override fun heat() {
		println("~ ~ ~ heating ~ ~ ~")
	}
}
