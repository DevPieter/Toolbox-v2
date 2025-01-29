package nl.devpieter.toolbox.Widgets.Abstractions;

import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.abstractions.IWidgetSetting;

public abstract class SettingWidgetBase<T, S extends IWidgetSetting<T>> extends WidgetBase {

    protected final SettingManager settingManager = SettingManager.getInstance();

    protected final ISettingModule module;
    protected final S setting;

    public SettingWidgetBase(float x, float y, float width, float height, ISettingModule module, S setting) {
        super(x, y, width, height);
        this.module = module;
        this.setting = setting;
    }

    public void save() {
        settingManager.saveSetting(module, setting);
    }
}
