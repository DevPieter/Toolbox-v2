package nl.devpieter.toolbox.setting.abstractions;

import java.util.List;

public interface ISettingGroup {

    String getTitle();

    List<ISetting> getSettings();
}
