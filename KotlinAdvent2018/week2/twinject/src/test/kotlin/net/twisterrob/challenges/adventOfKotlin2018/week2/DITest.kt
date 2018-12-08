package net.twisterrob.challenges.adventOfKotlin2018.week2

import org.junit.jupiter.api.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DITest {

	@Test fun `registerSingleton produces the same value`() {
		class Dependency
		class Dependent(
			inject: Twinject,
			val dep: Dependency = inject()
		)

		val inject = (Twinject()){
			registerSingleton { Dependency() }
			register { Dependent(this) }
		}

		val dependent1: Dependent = inject()
		val dependent2: Dependent = inject()
		val dependency: Dependency = inject()

		assertNotSame(dependent1, dependent2)
		assertSame(dependency, dependent1.dep)
		assertSame(dependency, dependent2.dep)
	}
}
