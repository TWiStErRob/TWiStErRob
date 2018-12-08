package net.twisterrob.challenges.adventOfKotlin2018.week2.reflect

import net.twisterrob.challenges.adventOfKotlin2018.week2.Twinject
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class DITest {

	@Test fun `registerSingleton produces the same value`() {
		class Dependency

		val inject = (Twinject()){
			registerSingletonSelf<Dependency>()
		}

		val dependency1: Dependency = inject()
		val dependency2: Dependency = inject()

		assertSame(dependency1, dependency2)
	}

	@Test fun `registerSingleton produces the same value (class object)`() {
		class Dependency

		val inject = (Twinject()){
			registerSingleton(Dependency::class)
		}

		val dependency1: Dependency = inject()
		val dependency2: Dependency = inject()

		assertSame(dependency1, dependency2)
	}

	@Test fun `registerSingleton produces the same value for provided implementation`() {
		abstract class Contract
		class Impl : Contract()

		val inject = (Twinject()){
			registerSingleton<Contract, Impl>()
		}

		val dependency1: Contract = inject()
		val dependency2: Contract = inject()

		assertSame(dependency1, dependency2)
	}
}
