package nl.devpieter.toolbox.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberUtilsTest {

    @Test
    void floatingPointSettingsRetainFractionalValuesAndDoublePrecision() {
        Float floatValue = NumberUtils.cast(Float.class, 0.375f);
        Double doubleValue = NumberUtils.cast(Double.class, 0.123456789123d);
        assertEquals(0.375f, floatValue.floatValue());
        assertEquals(0.123456789123d, doubleValue.doubleValue());
    }

    @Test
    void integralSettingsRoundToNearestWholeValue() {
        Integer integerValue = NumberUtils.cast(Integer.class, 65.6f);
        Long longValue = NumberUtils.cast(Long.class, 65.6d);
        Short shortValue = NumberUtils.cast(Short.class, 65.6f);
        Byte byteValue = NumberUtils.cast(Byte.class, 65.6f);
        assertEquals(66, integerValue.intValue());
        assertEquals(66L, longValue.longValue());
        assertEquals((short) 66, shortValue.shortValue());
        assertEquals((byte) 66, byteValue.byteValue());
    }

    @Test
    void existingLongValuesDoNotPassThroughFloatingPoint() {
        Long value = NumberUtils.cast(Long.class, 9_007_199_254_740_993L);
        assertEquals(9_007_199_254_740_993L, value.longValue());
    }
}
