package net.twisterrob.test.jfixture.features;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.customisation.TracingCustomisation;

public class DebuggingTest {

	@Test void test() {
		JFixture fixture = new JFixture();
		fixture.customise(new TracingCustomisation(System.out));

		var data = fixture.create(CountDownLatch.class);

		assertNotNull(data);
	}

	@Test void testFailingConstructorFallsbackToLowerPriorityOne() {
		JFixture fixture = new JFixture();
		fixture.customise(new TracingCustomisation(System.out));

		var data = fixture.create(ClassWithFailingConstructor.class);

		assertNotNull(data);
	}

	@SuppressWarnings("unused")
	static class ClassWithFailingConstructor {

		ClassWithFailingConstructor(int ignore) {
			throw new RuntimeException("Simulate failure");
		}

		ClassWithFailingConstructor(int ignore, boolean fallback) {
			// allow creation
		}
	}
}
