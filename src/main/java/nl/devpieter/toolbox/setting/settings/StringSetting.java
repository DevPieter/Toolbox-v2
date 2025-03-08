package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.base.SettingBase;
import nl.devpieter.toolbox.setting.interfaces.IStringSetting;

public class StringSetting extends SettingBase<String> implements IStringSetting {

    public StringSetting(String identifier, String defaultValue) {
        super(identifier, defaultValue);
    }

    public StringSetting(String identifier, String defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }
}
