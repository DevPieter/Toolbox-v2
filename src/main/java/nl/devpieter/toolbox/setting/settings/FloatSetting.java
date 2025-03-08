package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.base.SettingBase;
import nl.devpieter.toolbox.setting.interfaces.INumberSetting;

import java.lang.reflect.Type;

public class FloatSetting extends SettingBase<Float> implements INumberSetting<Float> {

    public FloatSetting(String identifier, Float defaultValue) {
        super(identifier, defaultValue);
    }

    public FloatSetting(String identifier, Float defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public void increment() {
        setValue(getValue() + 1F);
    }

    @Override
    public void increment(Float amount) {
        setValue(getValue() + amount);
    }

    @Override
    public void decrement() {
        setValue(getValue() - 1F);
    }

    @Override
    public void decrement(Float amount) {
        setValue(getValue() - amount);
    }

    @Override
    public Type getType() {
        return Float.class;
    }
}
