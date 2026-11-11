package nl.devpieter.toolbox.utils;

import java.lang.reflect.Type;

public class NumberUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Type type, Number value) {
        if (type == Integer.class) {
            return (T) Integer.valueOf((int) roundIntegral(value));
        } else if (type == Long.class) {
            return (T) Long.valueOf(roundIntegral(value));
        } else if (type == Float.class) {
            return (T) Float.valueOf(value.floatValue());
        } else if (type == Double.class) {
            return (T) Double.valueOf(value.doubleValue());
        } else if (type == Short.class) {
            return (T) Short.valueOf((short) roundIntegral(value));
        } else if (type == Byte.class) {
            return (T) Byte.valueOf((byte) roundIntegral(value));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    private static long roundIntegral(Number value) {
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return value.longValue();
        }
        return Math.round(value.doubleValue());
    }
}
