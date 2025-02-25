package nl.devpieter.toolbox.setting.abstractions;

import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;

public interface IWidgetSetting<T> extends ISetting<T> {

    String getDisplayName();

    String getDescription();

    boolean isEditable();

    boolean isHidden();

    boolean isRequired();

    IComponent createWidget(IRenderable parent, float x, float y, float width, float gap, ISettingModule module);
}
