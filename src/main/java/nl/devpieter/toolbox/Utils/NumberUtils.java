package nl.devpieter.toolbox.Utils;

import java.lang.reflect.Type;

public class NumberUtils {

    // TODO - Optimize this?
    @SuppressWarnings("unchecked")
    public static <T> T cast(Type type, Number value) {
        float f = Math.round(value.floatValue());

        if (type == Integer.class) {
            return (T) Integer.valueOf((int) f);
        } else if (type == Long.class) {
            return (T) Long.valueOf((long) f);
        } else if (type == Float.class) {
            return (T) Float.valueOf(f);
        } else if (type == Double.class) {
            return (T) Double.valueOf(f);
        } else if (type == Short.class) {
            return (T) Short.valueOf((short) f);
        } else if (type == Byte.class) {
            return (T) Byte.valueOf((byte) f);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
