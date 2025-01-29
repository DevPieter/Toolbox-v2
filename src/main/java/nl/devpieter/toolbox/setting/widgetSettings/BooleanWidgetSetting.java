package nl.devpieter.toolbox.setting.widgetSettings;

import nl.devpieter.toolbox.setting.abstractions.WidgetSettingBase;

import java.lang.reflect.Type;

public class BooleanWidgetSetting extends WidgetSettingBase<Boolean> {

    public BooleanWidgetSetting(String identifier, Boolean defaultValue, String displayName, String description) {
        super(identifier, defaultValue, displayName, description);
    }

    public BooleanWidgetSetting(String identifier, Boolean defaultValue, String displayName, String description, boolean allowNull) {
        super(identifier, defaultValue, displayName, description, allowNull);
    }

    public BooleanWidgetSetting(String identifier, Boolean defaultValue, String displayName, String description, boolean allowNull, boolean editable, boolean hidden, boolean required) {
        super(identifier, defaultValue, displayName, description, allowNull, editable, hidden, required);
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
