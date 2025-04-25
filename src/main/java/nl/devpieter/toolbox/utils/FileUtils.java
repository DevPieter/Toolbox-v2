package nl.devpieter.toolbox.utils;

import nl.devpieter.toolbox.Toolbox;

import java.io.File;

public class FileUtils {

    public static boolean tryCreateDirectories(File file) {
        try {
            File parentDir = file.getParentFile();
            if (parentDir.exists()) return true;

            return parentDir.mkdirs();
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create directories for module file: {}", file.getAbsolutePath(), e);
            return false;
        }
    }

    public static boolean tryCreateFile(File file) {
        try {
            if (file.exists()) return true;
            if (!tryCreateDirectories(file)) return false;

            return file.createNewFile();
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to create module file: {}", file.getAbsolutePath(), e);
            return false;
        }
    }
}
