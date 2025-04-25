package nl.devpieter.toolbox;

import nl.devpieter.toolbox.setting.interfaces.ISetting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalSettings {

    private static GlobalSettings INSTANCE;

    private final List<ISetting<?>> settings = new ArrayList<>();

    private GlobalSettings() {
    }

    public static GlobalSettings getInstance() {
        if (INSTANCE == null) INSTANCE = new GlobalSettings();
        return INSTANCE;
    }

    public List<ISetting<?>> getSettings() {
        return this.settings;
    }

    public void addSetting(ISetting<?> setting) {
        this.settings.add(setting);
    }

    public void removeSetting(ISetting<?> setting) {
        this.settings.remove(setting);
    }

    public <T extends ISetting<?>> @Nullable T getSetting(String key, Class<T> settingClass) {
        try {
            ISetting<?> setting = this.getSettings().stream().filter(s -> s.getIdentifier().equals(key)).findFirst().orElse(null);
            if (!settingClass.isInstance(setting)) return null;

            return settingClass.cast(setting);
        } catch (Exception e) {

            return null;
        }
    }
}
