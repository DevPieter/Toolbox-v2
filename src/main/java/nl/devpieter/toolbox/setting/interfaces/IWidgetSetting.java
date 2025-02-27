package nl.devpieter.toolbox.setting.interfaces;

import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;

public interface IWidgetSetting<T, S extends ISetting<T>> {

    String getName();

    String getDescription();

    S getSetting();

    boolean isEditable();

    boolean isHidden();

    boolean isRequired();

    IComponent createWidget(float x, float y, float width, float gap, ISettingModule module);
}
