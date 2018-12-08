package net.twisterrob.challenges.adventOfKotlin2018.week2

fun main(args: Array<String>) {
	val injection = (Twinject()) {
		register<Pump> { Thermosiphon(this) }
		register<Heater> { EletrictHeater() }
		register() { CoffeeMaker(this) }
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
	override fun heat() = println("~ ~ ~ heating ~ ~ ~")
}
