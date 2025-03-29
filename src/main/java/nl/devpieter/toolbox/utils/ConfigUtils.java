package nl.devpieter.toolbox.utils;

import nl.devpieter.toolbox.module.interfaces.IModule;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfigUtils {

    public static @Nullable File getConfigFolder(boolean createIfNotExists) {
        File configFolder = new File("config/toolbox");

        boolean exists = configFolder.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateDirectories(configFolder) ? configFolder : null;
    }

    public static @Nullable File getSettingsFile(boolean createIfNotExists) {
        File configFolder = getConfigFolder(createIfNotExists);
        if (configFolder == null) return null;

        String fileName = "Settings.json";
        File settingsFile = new File(configFolder, fileName);

        boolean exists = settingsFile.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateFile(settingsFile) ? settingsFile : null;
    }

    public static @Nullable File getModulesFolder(boolean createIfNotExists) {
        File modulesFolder = new File("config/toolbox/modules");

        boolean exists = modulesFolder.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateDirectories(modulesFolder) ? modulesFolder : null;
    }

    public static @Nullable File getModuleFolder(Class<? extends IModule> moduleClass, boolean createIfNotExists) {
        File modulesFolder = getModulesFolder(createIfNotExists);
        if (modulesFolder == null) return null;

        String packageName = moduleClass.getPackageName().replace(".", "/");
        String className = moduleClass.getSimpleName();

        File moduleFolder = new File(modulesFolder, packageName + "/" + className);

        boolean exists = moduleFolder.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateDirectories(moduleFolder) ? moduleFolder : null;
    }

    // ex: nl/devpieter/toolbox/modules/MyModule/State.json
    public static @Nullable File getModuleStateFile(IModule module, boolean createIfNotExists) {
        File moduleFolder = getModuleFolder(module.getClass(), createIfNotExists);
        if (moduleFolder == null) return null;

        String fileName = "State.json";
        File stateFile = new File(moduleFolder, fileName);

        boolean exists = stateFile.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateFile(stateFile) ? stateFile : null;
    }

    // ex: nl/devpieter/toolbox/modules/MyModule/Settings.json
    public static @Nullable File getModuleSettingsFile(IModule module, boolean createIfNotExists) {
        File moduleFolder = getModuleFolder(module.getClass(), createIfNotExists);
        if (moduleFolder == null) return null;

        String fileName = "Settings.json";
        File settingsFile = new File(moduleFolder, fileName);

        boolean exists = settingsFile.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateFile(settingsFile) ? settingsFile : null;
    }
}
