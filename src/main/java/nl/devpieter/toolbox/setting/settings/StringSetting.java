package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.SettingBase;

import java.lang.reflect.Type;

public class StringSetting extends SettingBase<String> {

    public StringSetting(String identifier, String defaultValue) {
        super(identifier, defaultValue);
    }

    public StringSetting(String identifier, String defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public Type getType() {
        return String.class;
    }
}
