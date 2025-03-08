package nl.devpieter.toolbox.setting.interfaces;

import nl.devpieter.toolbox.setting.KeyedSetting;

import java.lang.reflect.Type;

public interface ISetting<T> {

    Type getType();

    String getIdentifier();

    boolean shouldAllowNull();

    T getValue();

    T getDefault();

    void setValue(T value);

    default void reset() {
        setValue(getDefault());
    }

    default boolean isDefaultValueSet() {
        return !getValue().equals(getDefault());
    }

    default KeyedSetting<T> asKeyedSetting() {
        return new KeyedSetting<>(getIdentifier(), getValue());
    }
}
