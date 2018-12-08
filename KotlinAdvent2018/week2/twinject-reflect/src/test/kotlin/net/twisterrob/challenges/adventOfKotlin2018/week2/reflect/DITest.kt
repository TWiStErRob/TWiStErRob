package net.twisterrob.challenges.adventOfKotlin2018.week2.reflect

import net.twisterrob.challenges.adventOfKotlin2018.week2.Twinject
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class DITest {

	@Test fun `register singleton produces the same value`() {
		class Dependency

		val inject = (Twinject()){
			register(singleton<Dependency>())
		}

		val dependency1: Dependency = inject()
		val dependency2: Dependency = inject()

		assertSame(dependency1, dependency2)
	}

	@Test fun `register singleton produces the same value (class object)`() {
		class Dependency

		val inject = (Twinject()){
			register(singleton(Dependency::class))
		}

		val dependency1: Dependency = inject()
		val dependency2: Dependency = inject()

		assertSame(dependency1, dependency2)
	}

	@Test fun `register singleton produces the same value for provided implementation`() {
		abstract class Contract
		class Impl : Contract()

		val inject = (Twinject()){
			register(singletonContract<Contract, Impl>())
		}

		val dependency1: Contract = inject()
		val dependency2: Contract = inject()

		assertSame(dependency1, dependency2)
	}
}
