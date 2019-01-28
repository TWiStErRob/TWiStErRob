package net.twisterrob.test.jfixture.features.runner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.annotations.Fixture;

/** gist of {@code @RunWith(JFixtureJUnitRunner.class)} */
@SuppressWarnings("JUnit5Converter")
public class NoRunnerTest {

	@Fixture String fixtString;

	@Before
	public void setUp() {
		FixtureAnnotations.initFixtures(this);
	}

	@Test public void test() {
		assertNotNull(fixtString);
	}
}
