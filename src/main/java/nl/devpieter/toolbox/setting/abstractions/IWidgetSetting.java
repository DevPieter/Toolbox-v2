package nl.devpieter.toolbox.setting.abstractions;

public interface IWidgetSetting<T> extends ISetting<T> {

    String getDisplayName();

    String getDescription();

    boolean isEditable();

    boolean isHidden();

    boolean isRequired();
}
