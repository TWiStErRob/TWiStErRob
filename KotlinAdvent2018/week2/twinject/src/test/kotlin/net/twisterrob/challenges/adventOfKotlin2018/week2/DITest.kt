package net.twisterrob.challenges.adventOfKotlin2018.week2

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DITest {

	@Test fun `inject provided class`() {
		class Dependency

		val inject = (Twinject()){
			register { Dependency() }
		}

		@Suppress("UNUSED_VARIABLE")
		val dependency: Dependency = inject()

		// it compiles and runs (doesn't throw), that's the verification
	}

	@Test fun `inject provided object to nullable`() {
		class Dependency

		val inject = (Twinject()){
			register { Dependency() }
		}

		val dependency: Dependency? = inject()

		assertNotNull(dependency)
	}

	@Test fun `inject provided class as contract`() {
		abstract class Contract
		class Impl : Contract()

		val inject = (Twinject()){
			register<Contract> { Impl() }
		}

		val dependency: Contract = inject()

		assertTrue(dependency is Impl)
	}

	@Test fun `inject by field injection`() {
		class Dependency
		class Dependent(inject: Twinject) {
			val dep: Dependency by inject
		}

		val inject = (Twinject()){
			register { Dependency() }
			register { Dependent(this) }
		}

		val dependency: Dependent = inject()

		dependency.dep
	}

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
