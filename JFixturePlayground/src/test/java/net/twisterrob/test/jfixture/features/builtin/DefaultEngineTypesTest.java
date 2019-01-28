package net.twisterrob.test.jfixture.features.builtin;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.MultipleCount;
import com.flextrade.jfixture.annotations.Fixture;

/**
 * @see com.flextrade.jfixture.builders.DefaultEngineParts
 */
@SuppressWarnings("unused")
public class DefaultEngineTypesTest {

	//region String
	/**
	 * @see com.flextrade.jfixture.builders.StringGenerator
	 * @see com.flextrade.jfixture.builders.SeededStringBuilder
	 * @see com.flextrade.jfixture.builders.SeedIgnoringRelay
	 */
	@Fixture java.lang.String fixtString;
	//endregion

	//region Primitives and Wrappers
	/** @see com.flextrade.jfixture.builders.NumberInRangeGenerator */
	@Fixture byte fixtPrimitiveByte;
	@Fixture java.lang.Byte fixtWrapperByte;

	@Fixture short fixtPrimitiveShort;
	@Fixture java.lang.Short fixtWrapperShort;

	@Fixture int fixtPrimitiveInt;
	@Fixture java.lang.Integer fixtWrapperInteger;

	@Fixture long fixtPrimitiveLong;
	@Fixture java.lang.Long fixtWrapperLong;

	@Fixture float fixtPrimitiveFloat;
	@Fixture java.lang.Float fixtWrapperFloat;

	@Fixture double fixtPrimitiveDouble;
	@Fixture java.lang.Double fixtWrapperDouble;

	/** @see com.flextrade.jfixture.builders.SwitchingBooleanGenerator */
	@Fixture boolean fixtPrimitiveBoolean;
	@Fixture java.lang.Boolean fixtWrapperBoolean;

	/** @see com.flextrade.jfixture.builders.CharacterGenerator */
	@Fixture char fixtPrimitiveChar;
	@Fixture java.lang.Character fixtWrapperChar;
	//endregion

	//region Java
	/** @see com.flextrade.jfixture.builders.NumberInRangeGenerator */
	@Fixture java.math.BigDecimal fixtBigDecimal;

	/** @see com.flextrade.jfixture.builders.NumberInRangeGenerator */
	@Fixture java.math.BigInteger fixtBigInteger;

	/** @see com.flextrade.jfixture.builders.UuidGenerator */
	@Fixture java.util.UUID fixtUuid;

	/** @see com.flextrade.jfixture.builders.DateGenerator */
	@Fixture java.util.Date fixtDate;

	/** @see com.flextrade.jfixture.builders.CalendarBuilder */
	@Fixture java.util.Calendar fixtCalendar;

	/** @see com.flextrade.jfixture.builders.EnumBuilder */
	@Fixture TimeUnit fixtEnumClass;

	/** @see com.flextrade.jfixture.builders.UrlBuilder */
	@Fixture java.net.URL fixtUrl;

	/** @see com.flextrade.jfixture.builders.UriBuilder */
	@Fixture java.net.URI fixtUri;
	//endregion

	//region Collections
	/** @see com.flextrade.jfixture.builders.MultipleSpecimenRelay */
	@Fixture MultipleCount fixtCount;

	/** @see com.flextrade.jfixture.builders.ArrayRelay */
	@Fixture Object[] fixtArray;

	/**
	 * @see com.flextrade.jfixture.builders.MapBuilder
	 * @see com.flextrade.jfixture.builders.MapRelay
	 * @see com.flextrade.jfixture.builders.KeyValuePairRelay
	 */
	@Fixture java.util.Map<Object, Object> fixtMap; // HashMap

	/** @see com.flextrade.jfixture.builders.QueueBuilder */
	@Fixture java.util.Queue<Object> fixtQueue; // ArrayDeque

	/** @see com.flextrade.jfixture.builders.SetBuilder */
	@Fixture java.util.Set<Object> fixtSet; // HashSet

	/**
	 * @see com.flextrade.jfixture.builders.IterableBuilder
	 * @see com.flextrade.jfixture.builders.IterableRelay
	 */
	@Fixture java.lang.Iterable<Object> fixtIterable; // ArrayList
	//endregion

	@BeforeEach void setUp() {
		FixtureAnnotations.initFixtures(this);
	}

	@Test void test() {
		assertEquals(3, fixtArray.length);
		assertEquals(3, fixtMap.size());
		assertEquals(3, fixtQueue.size());
		assertEquals(3, fixtSet.size());
		assertEquals(3, fixtIterable.spliterator().getExactSizeIfKnown());
	}
}
