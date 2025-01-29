package nl.devpieter.toolbox.setting.abstractions;

public abstract class WidgetSettingBase<T> implements IWidgetSetting<T> {

    private final String identifier;
    private final T defaultValue;

    private String displayName;
    private String description;

    private boolean allowNull;

    private boolean editable;
    private boolean hidden;
    private boolean required;

    private T value;

    public WidgetSettingBase(String identifier, T defaultValue, String displayName, String description) {
        this(identifier, defaultValue, displayName, description, false);
    }

    public WidgetSettingBase(String identifier, T defaultValue, String displayName, String description, boolean allowNull) {
        this(identifier, defaultValue, displayName, description, allowNull, true, false, false);
    }

    public WidgetSettingBase(String identifier, T defaultValue, String displayName, String description, boolean allowNull, boolean editable, boolean hidden, boolean required) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
        this.displayName = displayName;
        this.description = description;
        this.allowNull = allowNull;
        this.editable = editable;
        this.hidden = hidden;
        this.required = required;
        this.value = defaultValue;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean shouldAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
