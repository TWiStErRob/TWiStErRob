package net.twisterrob.test.jfixture.features

import com.flextrade.jfixture.JFixture
import org.junit.jupiter.api.Test
import java.lang.Math.random
import kotlin.math.roundToInt

class CustomizationsTest {
	@Test fun test() {
		val fixture = JFixture()
		fixture
			.customise()
			.sameInstance(Long::class.java, 20)
			.lazyInstance(Double::class.java) { (random() * 10).roundToInt() / 10.0 }
			.intercept(Data::class.java) { data ->
				data.position = fixture.create().inRange(Int::class.java, 0, data.text.length)
			}
			.repeatCount(5)
			.propertyOf(Data::class.java, "text", "foobar")

		val data: List<@JvmSuppressWildcards Data> = fixture()
		println(data.toString().replace(", text", "\ntext"))
	}

	@Suppress("MemberVisibilityCanBePrivate")
	class Data(
		var text: String,
		var position: Int,
		val probability: Double,
		scale: Long
	) {

		val scaledProbability = scale * probability

		override fun toString() =
			"text=$text, pos=$position, prob=$probability($scaledProbability)"
	}
}
