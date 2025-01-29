package nl.devpieter.toolbox.Module.Interfaces;

import nl.devpieter.toolbox.setting.abstractions.ISetting;

import java.util.List;

public interface ISettingModule extends IModule {

    List<ISetting<?>> getSettings();
}
