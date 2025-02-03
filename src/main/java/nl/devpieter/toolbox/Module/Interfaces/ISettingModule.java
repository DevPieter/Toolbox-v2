package nl.devpieter.toolbox.Module.Interfaces;

import nl.devpieter.toolbox.setting.abstractions.ISetting;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;

import java.util.ArrayList;
import java.util.List;

public interface ISettingModule extends IModule {

    default List<ISetting<?>> getSettings() {
        return new ArrayList<>();
    }

    default List<ISettingGroup> getSettingGroups() {
        return new ArrayList<>();
    }
}
