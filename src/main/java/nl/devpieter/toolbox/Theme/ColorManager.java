package nl.devpieter.toolbox.Theme;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Utils.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class ColorManager {

    private static final ColorManager INSTANCE = new ColorManager();

    public static ColorManager getInstance() {
        return INSTANCE;
    }

    private ColorManager() {
        this.load();
    }

    private final Gson gson = new Gson();
    private final HashMap<String, Color> loadedColors = new HashMap<>();
    private final Type colorMapType = new TypeToken<HashMap<String, String>>() {
    }.getType();

    public void load() {
        File colorsFolder = ResourceUtils.getResourceFolder("colors");
        if (colorsFolder == null) {
            Toolbox.LOGGER.error("Failed to load colors! (Colors folder not found)");
            return;
        }

        File[] files = colorsFolder.listFiles();
        if (files == null) {
            Toolbox.LOGGER.error("Failed to load colors! (Colors folder is empty)");
            return;
        }

        for (File file : files) {
            try {
                HashMap<String, String> colorMap = gson.fromJson(new FileReader(file), this.colorMapType);

                for (String key : colorMap.keySet()) {
                    String finalKey = file.getName().replace(".json", "") + "." + key;
                    loadedColors.put(finalKey, this.translateColor(colorMap.get(key)));
                }
            } catch (Exception e) {
                Toolbox.LOGGER.warn("Failed to load colors from file {}", file.getName(), e);
            }
        }
    }

    public Color translateColor(String colorString) {
        colorString = colorString.replace("#", "").replace("0x", "");
        if (colorString.length() != 6 && colorString.length() != 8) return Color.WHITE;

        try {
            boolean hasAlpha = colorString.length() == 8;

            int a = hasAlpha ? Integer.parseInt(colorString.substring(0, 2), 16) : 255;
            int r = Integer.parseInt(colorString.substring(hasAlpha ? 2 : 0, hasAlpha ? 4 : 2), 16);
            int g = Integer.parseInt(colorString.substring(hasAlpha ? 4 : 2, hasAlpha ? 6 : 4), 16);
            int b = Integer.parseInt(colorString.substring(hasAlpha ? 6 : 4, hasAlpha ? 8 : 6), 16);

            return new Color(r, g, b, a);
        } catch (Exception e) {
            Toolbox.LOGGER.warn("Failed to decode color {}, use #RRGGBB or #AARRGGBB", colorString, e);
            return Color.WHITE;
        }
    }

    public Color get(String key) {
        return this.get(key, Color.WHITE);
    }

    public Color get(String key, Color fallback) {
        return loadedColors.getOrDefault(key, fallback);
    }

    public int getRGB(String key) {
        return this.getRGB(key, 0xFFFFFF);
    }

    public int getRGB(String key, int fallback) {
        Color color = loadedColors.get(key);
        if (color == null) return fallback;

        return color.getRGB();
    }

    public float[] getFloats(String key) {
        return this.getFloats(key, new float[]{1, 1, 1, 1});
    }

    public float[] getFloats(String key, float[] fallback) {
        Color color = loadedColors.get(key);
        if (color == null) return fallback;

        return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
    }
}
