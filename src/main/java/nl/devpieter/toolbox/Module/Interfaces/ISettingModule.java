package nl.devpieter.toolbox.Module.Interfaces;

import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.interfaces.ISetting;

import java.util.ArrayList;
import java.util.List;

public interface ISettingModule extends IModule {

    default List<ISetting<?>> getSettings() {
        return new ArrayList<>();
    }

    default List<SettingGroup> getSettingGroups() {
        return new ArrayList<>();
    }
}
