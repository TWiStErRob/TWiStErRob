package net.twisterrob.test.jfixture.features.runner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;

/**
 * JUnit 5, customization, control of order
 */
public class NoRunnerBenefitsTest {

	@Fixture String fixtString;

	@BeforeEach
	public void setUp() {
		JFixture fixture = new JFixture();
		fixture.customise().sameInstance(String.class, "");
		FixtureAnnotations.initFixtures(this, fixture);
	}

	@Test void testCustomization() {
		assertEquals("", fixtString);
	}
}
