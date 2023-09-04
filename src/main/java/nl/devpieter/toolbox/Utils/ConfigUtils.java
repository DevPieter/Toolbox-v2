package nl.devpieter.toolbox.Utils;

import nl.devpieter.toolbox.Module.Interfaces.IStatefulModule;
import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfigUtils {

    public static @Nullable File getConfigFolder(boolean createIfNotExists) {
        File configFolder = new File("config/toolbox");

        try {
            boolean exists = configFolder.exists();
            if (!exists && !createIfNotExists) return null;
            if (!exists && !configFolder.mkdirs()) throw new Exception("Failed to create config folder!");

            return configFolder;
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create config folder {}", configFolder.getAbsolutePath(), e);
            return null;
        }
    }

    public static @Nullable File getModulesFolder(boolean createIfNotExists) {
        File modulesFolder = new File("config/toolbox/modules");

        try {
            boolean exists = modulesFolder.exists();
            if (!exists && !createIfNotExists) return null;
            if (!exists && !modulesFolder.mkdirs()) throw new Exception("Failed to create modules folder!");

            return modulesFolder;
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create modules folder {}", modulesFolder.getAbsolutePath(), e);
            return null;
        }
    }

    public static @Nullable File getConfigFile(String fileName, boolean createIfNotExists) {
        File configFolder = getConfigFolder(createIfNotExists);
        if (configFolder == null) return null;

        File configFile = new File(configFolder, fileName);

        try {
            boolean exists = configFile.exists();
            if (!exists && !createIfNotExists) return null;
            if (!exists && !configFile.createNewFile()) throw new Exception("Failed to create config file!");

            return configFile;
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create config file {}", configFile.getAbsolutePath(), e);
            return null;
        }
    }

    public static @Nullable File getModuleFile(IStatefulModule<?> module, boolean createIfNotExists) {
        File modulesFolder = getModulesFolder(createIfNotExists);
        if (modulesFolder == null) return null;

        File moduleFile = new File(modulesFolder, module.getFileName());

        try {
            boolean exists = moduleFile.exists();
            if (!exists && !createIfNotExists) return null;
            if (!exists && !moduleFile.createNewFile()) throw new Exception("Failed to create module file!");

            return moduleFile;
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create module file {}", moduleFile.getAbsolutePath(), e);
            return null;
        }
    }

}
