package nl.devpieter.toolbox.setting.widgetSettings;

import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.toolbox.module.interfaces.ISettingModule;
import nl.devpieter.toolbox.widgets.settingWidgets.BooleanSettingWidget;
import nl.devpieter.toolbox.setting.base.WidgetSettingBase;
import nl.devpieter.toolbox.setting.interfaces.IBooleanSetting;

public class BooleanWidgetSetting extends WidgetSettingBase<Boolean, IBooleanSetting> {

    public BooleanWidgetSetting(String name, String description, IBooleanSetting setting) {
        super(name, description, setting);
    }

    public BooleanWidgetSetting(String name, String description, IBooleanSetting setting, boolean editable, boolean hidden, boolean required) {
        super(name, description, setting, editable, hidden, required);
    }

    @Override
    public IComponent createWidget(float x, float y, float width, float gap, ISettingModule module) {
        return new BooleanSettingWidget(x, y, width, gap, module, this);
    }
}
