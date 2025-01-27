package nl.devpieter.toolbox.setting;

public interface ISetting<T> {

    Class<T> getType();

    String getIdentifier();

    boolean allowNull();

    T getValue();

    T getDefault();

    void setValue(T value);

    default KeyedSetting<T> asKeyedSetting() {
        return new KeyedSetting<>(getIdentifier(), getValue());
    }
}
