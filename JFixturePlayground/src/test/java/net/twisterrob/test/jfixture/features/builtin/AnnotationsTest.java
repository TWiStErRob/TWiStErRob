package net.twisterrob.test.jfixture.features.builtin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.annotations.Fixture;
import com.flextrade.jfixture.annotations.FromListOf;
import com.flextrade.jfixture.annotations.Range;

@SuppressWarnings({"unused", "SSBasedInspection"})
public class AnnotationsTest {

	/**
	 * @see com.flextrade.jfixture.builders.StringGenerator
	 * @see com.flextrade.jfixture.builders.SeededStringBuilder
	 * @see com.flextrade.jfixture.builders.SeedIgnoringRelay
	 */
	@Fixture java.lang.String string;

	/** @see com.flextrade.jfixture.builders.ElementFromListRelay */
	@Fixture @FromListOf(strings = {"a", "b", "c"})
	String stringFromList;

	/** @see com.flextrade.jfixture.builders.ElementFromListRelay */
	@Fixture @FromListOf(numbers = {1, 2, 3})
	int numberFromList;

	/** @see com.flextrade.jfixture.builders.NumericRangeRelay */
	@Fixture @Range(min = 1, max = 3)
	int numberInRange;

	/** @see com.flextrade.jfixture.builders.DateRangeRelay */
	@Fixture @Range(min = 0, max = 946684800000L)
	java.util.Date dateInRange;

	/** @see com.flextrade.jfixture.builders.CalendarRangeRelay */
//	@Fixture @Range(min = 0, max = 946684800000L)
	java.util.Calendar calendar; // not supported with range annotation

	@BeforeEach void setUp() {
		FixtureAnnotations.initFixtures(this);
	}

	@Test void test() {
	}
}
