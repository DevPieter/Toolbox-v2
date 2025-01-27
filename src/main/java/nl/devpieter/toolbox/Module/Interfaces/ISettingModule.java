package nl.devpieter.toolbox.Module.Interfaces;

import nl.devpieter.toolbox.setting.ISetting;

import java.util.List;

public interface ISettingModule extends IModule {

    List<ISetting<?>> getSettings();

    default String getFilePath() {
        String path = this.getClass().getPackageName().replace(".", "/");
        String name = this.getClass().getSimpleName();

        return path + "/" + name + ".json";
    }
}
