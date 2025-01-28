package nl.devpieter.toolbox.setting;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Utils.ConfigUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SettingManager {

    private static SettingManager INSTANCE;

    private final Gson gson = new Gson();

    private SettingManager() {
    }

    public static SettingManager getInstance() {
        if (INSTANCE == null) INSTANCE = new SettingManager();
        return INSTANCE;
    }

    public boolean saveSettings(ISettingModule module) {
        File moduleFile = ConfigUtils.getModuleSettingsFile(module, true);
        if (moduleFile == null) return false;

        for (ISetting<?> setting : module.getSettings()) {
            saveSetting(moduleFile, setting);
        }

        return true;
    }

    public boolean saveSetting(ISettingModule module, ISetting<?> setting) {
        File moduleFile = ConfigUtils.getModuleSettingsFile(module, true);
        if (moduleFile == null) return false;

        return saveSetting(moduleFile, setting);
    }

    public boolean loadSettings(ISettingModule module) {
        File moduleFile = ConfigUtils.getModuleSettingsFile(module, false);
        if (moduleFile == null) return false;

        for (ISetting<?> setting : module.getSettings()) {
            loadSetting(moduleFile, setting);
        }

        return true;
    }

    public <T> boolean loadSetting(ISettingModule module, ISetting<T> setting) {
        File moduleFile = ConfigUtils.getModuleSettingsFile(module, false);
        if (moduleFile == null) return false;

        return loadSetting(moduleFile, setting);
    }

    private boolean saveSetting(File file, ISetting<?> setting) {
        List<KeyedSetting<?>> settingsList = loadSettings(file);
        if (settingsList == null) settingsList = new ArrayList<>();

        settingsList.removeIf(s -> s.key().equals(setting.getIdentifier()));
        settingsList.add(setting.asKeyedSetting());

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(settingsList, writer);
            return true;
        } catch (IOException e) {
            Toolbox.LOGGER.error("Failed to save settings to file: {}", file.getAbsolutePath(), e);
            return false;
        }
    }

    private <T> boolean loadSetting(File file, ISetting<T> setting) {
        List<KeyedSetting<?>> settingsList = loadSettings(file);

        if (settingsList == null) {
            Toolbox.LOGGER.warn("Failed to load settings from file, using default value");

            setting.setValue(setting.getDefault());
            return false;
        }

        for (KeyedSetting<?> keyedSetting : settingsList) {
            if (!keyedSetting.key().equals(setting.getIdentifier())) continue;

            JsonElement jsonElement = gson.toJsonTree(keyedSetting.value());
            T value = gson.fromJson(jsonElement, setting.getType());

            setting.setValue(setting.allowNull() ? value : value != null ? value : setting.getDefault());
            return true;
        }

        return false;
    }

    private @Nullable List<KeyedSetting<?>> loadSettings(File file) {
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<KeyedSetting<?>>>() {
            }.getType());
        } catch (IOException e) {
            Toolbox.LOGGER.error("Failed to load settings from file: {}", file.getAbsolutePath(), e);
            return null;
        }
    }
}
