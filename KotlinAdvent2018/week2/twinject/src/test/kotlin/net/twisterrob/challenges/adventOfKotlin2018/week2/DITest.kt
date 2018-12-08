package net.twisterrob.challenges.adventOfKotlin2018.week2

import org.junit.jupiter.api.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DITest {

	@Test fun `register singleton produces the same value`() {
		class Dependency

		val inject = (Twinject()){
			register(singleton { Dependency() })
		}

		val dependency1: Dependency = inject()
		val dependency2: Dependency = inject()

		assertSame(dependency1, dependency2)
	}

	@Test fun `register singleton produces the same value when injected`() {
		class Dependency
		class Dependent(
			inject: Twinject,
			val dep: Dependency = inject()
		)

		val inject = (Twinject()){
			register(singleton { Dependency() })
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
