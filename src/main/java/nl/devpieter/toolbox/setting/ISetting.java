package nl.devpieter.toolbox.setting;

import java.lang.reflect.Type;

public interface ISetting<T> {

    Type getType();

    String getIdentifier();

    boolean shouldAllowNull();

    T getValue();

    T getDefault();

    void setValue(T value);

    default KeyedSetting<T> asKeyedSetting() {
        return new KeyedSetting<>(getIdentifier(), getValue());
    }
}
