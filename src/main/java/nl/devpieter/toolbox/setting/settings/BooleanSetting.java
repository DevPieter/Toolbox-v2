package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.abstractions.SettingBase;

import java.lang.reflect.Type;

public class BooleanSetting extends SettingBase<Boolean> {

    public BooleanSetting(String identifier, Boolean defaultValue) {
        super(identifier, defaultValue);
    }

    public BooleanSetting(String identifier, Boolean defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public Type getType() {
        return Boolean.class;
    }

    public void toggle() {
        setValue(!getValue());
    }

    public void setTrue() {
        setValue(true);
    }

    public void setFalse() {
        setValue(false);
    }
}
