package nl.devpieter.toolbox.setting.abstractions;

public abstract class WidgetSettingBase<T> extends SettingBase<T> implements IWidgetSetting<T> {

    private final String displayName;
    private final String description;

    private final boolean editable;
    private final boolean hidden;
    private final boolean required;

    public WidgetSettingBase(String identifier, T defaultValue, String displayName, String description) {
        this(identifier, defaultValue, displayName, description, true, false, false);
    }

    public WidgetSettingBase(String identifier, T defaultValue, String displayName, String description, boolean editable, boolean hidden, boolean required) {
        super(identifier, defaultValue);
        this.displayName = displayName;
        this.description = description;
        this.editable = editable;
        this.hidden = hidden;
        this.required = required;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
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
}
