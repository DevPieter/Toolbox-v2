package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.SettingBase;

import java.lang.reflect.Type;

public class IntSetting extends SettingBase<Integer> {

    public IntSetting(String identifier, Integer defaultValue) {
        super(identifier, defaultValue);
    }

    public IntSetting(String identifier, Integer defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public Type getType() {
        return Integer.class;
    }

    public void increment() {
        increment(1);
    }

    public void increment(int amount) {
        setValue(getValue() + amount);
    }

    public void decrement() {
        decrement(1);
    }

    public void decrement(int amount) {
        setValue(getValue() - amount);
    }
}
