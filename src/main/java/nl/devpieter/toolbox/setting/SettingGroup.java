package nl.devpieter.toolbox.setting;

import nl.devpieter.toolbox.setting.abstractions.ISetting;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;

import java.util.List;

public class SettingGroup implements ISettingGroup {

    private final String title;
    private final List<ISetting> settings;

    public SettingGroup(String title, List<ISetting> settings) {
        this.title = title;
        this.settings = settings;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<ISetting> getSettings() {
        return settings;
    }
}
