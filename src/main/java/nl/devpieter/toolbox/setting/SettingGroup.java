package nl.devpieter.toolbox.setting;

import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;
import nl.devpieter.toolbox.setting.abstractions.IWidgetSetting;

import java.util.List;

public record SettingGroup(String title, List<IWidgetSetting<?>> settings) implements ISettingGroup {
}
