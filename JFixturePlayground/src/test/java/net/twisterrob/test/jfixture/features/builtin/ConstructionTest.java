package net.twisterrob.test.jfixture.features.builtin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flextrade.jfixture.JFixture;

public class ConstructionTest {

	/**
	 * @see com.flextrade.jfixture.builders.GenericMethodBuilder
	 * @see com.flextrade.jfixture.builders.GenericFieldRelay
	 * @see com.flextrade.jfixture.builders.FieldRelay
	 *
	 * @see com.flextrade.jfixture.builders.GenericConstructorRelay
	 * @see com.flextrade.jfixture.builders.ClassToConstructorRelay
	 *
	 * @see com.flextrade.jfixture.builders.FactoryMethodRelay
	 * @see com.flextrade.jfixture.builders.ClassToFactoryMethodRelay
	 */
	@SuppressWarnings("unused")
	@Test void testTypesOfCreation() {
		JFixture fixture = new JFixture();
		var publicVisible = fixture.create(ClassWithPublicConstructor.class);
		var defaultVisible = fixture.create(ClassWithDefaultVisibleConstructor.class);
		var factory = fixture.create(ClassWithFactory.class);
	}

	//@formatter:off
	public static class ClassWithPublicConstructor {
		public ClassWithPublicConstructor() {}
	}

	public static class ClassWithDefaultVisibleConstructor {
		ClassWithDefaultVisibleConstructor() {}
	}

	public static class ClassWithFactory {
		private ClassWithFactory() {}
		@SuppressWarnings("unused")
		public static ClassWithFactory factory() {
			return new ClassWithFactory();
		}
	}
	//@formatter:on

	@Test void testOrder() {
		JFixture fixture = new JFixture();
		var creationType = fixture.create(ClassWithAllTypesOfCreation.class).type;

		// public, factory, default, error
		assertEquals("public", creationType);

		// java.lang.UnsupportedOperationException: JFixture was unable to create an instance of FQCN
		// Most likely because it has no public constructor, is an abstract or non-public type or has no static factory methods.
		// If this isn't the case it's likely that all constructors and factory methods on the type have thrown an exception.
		// 		To view any thrown exceptions just add the Tracing Customisation to the JFixture instance, e.g. fixture.customise(new TracingCustomisation(System.out));
		//
		// FQCN -->
		// 		FQCN
		//
		// at com.flextrade.jfixture.behaviours.noresolution.ThrowingNoResolutionHandler.handleNoResolution()
		// at Test.test()

		// TracingCustomisation reveals ObjectCreationException
	}

	@SuppressWarnings("unused")
	public static class ClassWithAllTypesOfCreation {

		private final String type;

		public ClassWithAllTypesOfCreation(int ignore) {
			this("public");
		}

		ClassWithAllTypesOfCreation(double ignore) {
			this("default");
		}

		private ClassWithAllTypesOfCreation(String type) {
			this.type = type;
		}

		public static ClassWithAllTypesOfCreation factory() {
			return new ClassWithAllTypesOfCreation("factory");
		}
	}
}
