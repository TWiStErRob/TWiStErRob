package net.twisterrob.test.jfixture.features.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

import com.flextrade.jfixture.annotations.Fixture;
import com.flextrade.jfixture.runners.JFixtureJUnitRunner;

@RunWith(JFixtureJUnitRunner.class)
public class RunnerTest {

	@Fixture String fixtString;

	@Test public void test() {
		assertNotNull(fixtString);
	}
}
