package nl.devpieter.toolbox.setting;

import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;
import nl.devpieter.toolbox.setting.abstractions.IWidgetSetting;

import java.util.List;

public class SettingGroup implements ISettingGroup {

    private final String title;
    private final List<IWidgetSetting<?>> settings;

    public SettingGroup(String title, List<IWidgetSetting<?>> settings) {
        this.title = title;
        this.settings = settings;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<IWidgetSetting<?>> getSettings() {
        return settings;
    }
}
