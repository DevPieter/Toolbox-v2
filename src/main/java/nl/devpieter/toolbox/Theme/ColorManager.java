package nl.devpieter.toolbox.Theme;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Utils.ResourceUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorManager implements SynchronousResourceReloader {

    private static ColorManager INSTANCE;

    private final HashMap<String, Color> loadedColors = new HashMap<>();

    private ColorManager() {
    }

    public static ColorManager getInstance() {
        if (INSTANCE == null) INSTANCE = new ColorManager();
        return INSTANCE;
    }

    @Override
    public void reload(ResourceManager manager) {
        this.loadedColors.clear();

        Map<Identifier, List<Resource>> map = manager.findAllResources("theme", (path) -> {
            if (!path.getNamespace().equals("toolbox")) return false;
            return path.getPath().endsWith("colors.json");
        });

        for (Map.Entry<Identifier, List<Resource>> entry : map.entrySet()) {
            for (Resource resource : entry.getValue()) {

                try {
                    ImmutableMap<String, String> rawColors = ResourceUtils.readJson(resource.getInputStream());
                    for (String key : rawColors.keySet()) {
                        loadedColors.put(key, this.translateColor(rawColors.get(key)));
                    }
                } catch (Exception e) {
                    Toolbox.LOGGER.error("Failed to load color overrides!", e);
                }
            }
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

    private Color translateColor(String colorString) {
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
}
