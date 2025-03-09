package nl.devpieter.toolbox.setting.base;

import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

public abstract class WidgetSettingBase<T, S extends ISetting<T>> implements IWidgetSetting<T, S> {

    private final String name;
    private final String description;

    private final S setting;

    private final boolean editable;
    private final boolean hidden;
    private final boolean required;

    public WidgetSettingBase(String name, String description, S setting) {
        this(name, description, setting, true, false, false);
    }

    public WidgetSettingBase(String name, String description, S setting, boolean editable, boolean hidden, boolean required) {
        this.name = name;
        this.description = description;
        this.setting = setting;
        this.editable = editable;
        this.hidden = hidden;
        this.required = required;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public S getSetting() {
        return setting;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public T getValue() {
        return setting.getValue();
    }

    public void setValue(T value) {
        setting.setValue(value);
    }
}
