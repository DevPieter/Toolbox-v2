package nl.devpieter.toolbox.setting;

import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.util.List;

public record SettingGroup(String title, List<IWidgetSetting<?, ?>> settings) {
}
