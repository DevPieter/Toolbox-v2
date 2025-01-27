package nl.devpieter.toolbox.setting;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Utils.ConfigUtils;

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

    public void loadSettings(ISettingModule module) {
        File base = ConfigUtils.getModulesFolder(false);
        if (base == null) return;

        String modulePath = module.getFilePath();
        File moduleFile = new File(base, modulePath);

        for (ISetting<?> setting : module.getSettings()) {
            loadSetting(moduleFile.getAbsolutePath(), setting);
        }
    }

    public void saveSettings(ISettingModule module) {
        File base = ConfigUtils.getModulesFolder(true);
        if (base == null) return;

        String modulePath = module.getFilePath();
        File moduleFile = new File(base, modulePath);

        try {
            File parentDir = moduleFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                Toolbox.LOGGER.error("Failed to create directories for module file: {}", moduleFile.getAbsolutePath());
                return;
            }

            if (!moduleFile.exists() && !moduleFile.createNewFile()) {
                Toolbox.LOGGER.error("Failed to create module file: {}", moduleFile.getAbsolutePath());
                return;
            }
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create module file: {}", moduleFile.getAbsolutePath(), e);
            return;
        }

        for (ISetting<?> setting : module.getSettings()) {
            saveSetting(moduleFile.getAbsolutePath(), setting);
        }
    }

    public void saveSetting(ISettingModule module, ISetting<?> setting) {
        File base = ConfigUtils.getModulesFolder(true);
        if (base == null) return;

        String modulePath = module.getFilePath();
        File moduleFile = new File(base, modulePath);

        saveSetting(moduleFile.getAbsolutePath(), setting);
    }

    public <T> void loadSetting(ISettingModule module, ISetting<T> setting) {
        File base = ConfigUtils.getModulesFolder(false);
        if (base == null) return;

        String modulePath = module.getFilePath();
        File moduleFile = new File(base, modulePath);

        loadSetting(moduleFile.getAbsolutePath(), setting);
    }

    private void saveSetting(String path, ISetting<?> setting) {
        File file = new File(path);

        List<KeyedSetting<?>> settingsList = loadSettings(file);
        if (settingsList == null) settingsList = new ArrayList<>();

        settingsList.removeIf(s -> s.key().equals(setting.getIdentifier()));
        settingsList.add(setting.asKeyedSetting());

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(settingsList, writer);
        } catch (IOException e) {
            Toolbox.LOGGER.error("Failed to save settings to file: {}", file.getAbsolutePath(), e);
        }
    }

    private <T> boolean loadSetting(String path, ISetting<T> setting) {
        File file = new File(path);

        List<KeyedSetting<?>> settingsList = loadSettings(file);
        if (settingsList == null) return false;

        for (KeyedSetting<?> keyedSetting : settingsList) {
            if (!keyedSetting.key().equals(setting.getIdentifier())) continue;

            JsonElement jsonElement = gson.toJsonTree(keyedSetting.value());
            T value = gson.fromJson(jsonElement, setting.getType());

            setting.setValue(setting.allowNull() ? value : value != null ? value : setting.getDefault());
            return true;
        }

        return false;
    }

    private List<KeyedSetting<?>> loadSettings(File file) {
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<KeyedSetting<?>>>() {
            }.getType());
        } catch (IOException e) {
            Toolbox.LOGGER.error("Failed to load settings from file: {}", file.getAbsolutePath(), e);
        }

        return new ArrayList<>();
    }
}
