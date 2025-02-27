package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.base.SettingBase;
import nl.devpieter.toolbox.setting.interfaces.INumberSetting;

import java.lang.reflect.Type;

public class IntSetting extends SettingBase<Integer> implements INumberSetting<Integer> {

    public IntSetting(String identifier, Integer defaultValue) {
        super(identifier, defaultValue);
    }

    public IntSetting(String identifier, Integer defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public void increment() {
        setValue(getValue() + 1);
    }

    @Override
    public void increment(Integer amount) {
        setValue(getValue() + amount);
    }

    @Override
    public void decrement() {
        setValue(getValue() - 1);
    }

    @Override
    public void decrement(Integer amount) {
        setValue(getValue() - amount);
    }

    @Override
    public Type getType() {
        return Integer.class;
    }
}
