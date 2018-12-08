package net.twisterrob.challenges.adventOfKotlin2018.week2.instantiate

import net.twisterrob.challenges.adventOfKotlin2018.week2.Twinject
import net.twisterrob.challenges.adventOfKotlin2018.week2.reflect.register
import net.twisterrob.challenges.adventOfKotlin2018.week2.reflect.registerContract

fun main(args: Array<String>) {
	val injection = (Twinject()) {
		// reflective creation (same type)
		register<CoffeeMaker>()
		// reflective creation (super type)
		registerContract<Pump, Thermosiphon>()
		// custom creation
		register<Heater> { EletrictHeater() }
	}

	val maker: CoffeeMaker = injection()
	maker.brew()
}

interface Pump {
	fun pump()
}

interface Heater {
	fun heat()
}

class Thermosiphon(
	inject: Twinject,
	private val heater: Heater = inject()
) : Pump {

	override fun pump() {
		heater.heat()
		println("=> => pumping => =>")
	}
}

class CoffeeMaker(
	inject: Twinject
) {

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
