package nl.devpieter.toolbox.setting.widgetSettings;

import nl.devpieter.toolbox.setting.abstractions.WidgetSettingBase;

import java.lang.reflect.Type;

public class NumberWidgetSetting<T extends Number> extends WidgetSettingBase<T> {

    private final Type type;

    private T min;
    private T max;

    public NumberWidgetSetting(String identifier, T defaultValue, String displayName, String description, Type type, T min, T max) {
        this(identifier, defaultValue, displayName, description, false, type, min, max);
    }

    public NumberWidgetSetting(String identifier, T defaultValue, String displayName, String description, boolean allowNull, Type type, T min, T max) {
        this(identifier, defaultValue, displayName, description, allowNull, true, false, false, type, min, max);
    }

    public NumberWidgetSetting(String identifier, T defaultValue, String displayName, String description, boolean allowNull, boolean editable, boolean hidden, boolean required, Type type, T min, T max) {
        super(identifier, defaultValue, displayName, description, allowNull, editable, hidden, required);
        this.type = type;
        this.min = min;
        this.max = max;
    }

    @Override
    public Type getType() {
        return type;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}
