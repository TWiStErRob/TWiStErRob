package net.twisterrob.test.jfixture.features;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.flextrade.jfixture.JFixture;

public class SimpleDataTest {

	public static class SimpleData {

		public String stringField;
	}

	@Test void testWithoutJFixture() {
		SimpleData data = new SimpleData();

		assertNull(data.stringField);
	}

	@Test void testWithJFixture() {
		JFixture fixture = new JFixture();

		SimpleData data = fixture.create(SimpleData.class);

		assertNotNull(data);
		assertNotNull(data.stringField);
	}
}
