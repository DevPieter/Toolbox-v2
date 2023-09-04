package nl.devpieter.toolbox.Theme;

import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;

public class ColorManager {

    private static final ColorManager INSTANCE = new ColorManager();

    public static ColorManager getInstance() {
        return INSTANCE;
    }

    private ColorManager() {
        this.load();
    }

    private final HashMap<String, Color> colors = new HashMap<>();

    public void load() {
        // TODO - Load from "assets/toolbox/colors/*.json"

        /* Text */
        colors.put("text.default", new Color(0xFFFFFF));
        colors.put("text.info", new Color(0x55FFFF));
        colors.put("text.success", new Color(0x55FF55));
        colors.put("text.warning", new Color(0xFFFF55));
        colors.put("text.error", new Color(0xFF5555));

        /* Widget */
        colors.put("widget.background", new Color(0x1A1A1A));
        colors.put("widget.background.hover", new Color(0x121212));
        colors.put("widget.background.disabled", new Color(0x222222));

        colors.put("widget.shadow", new Color(0x991A1A1A, true));
        colors.put("widget.shadow.hover", new Color(0xBF121212, true));
        colors.put("widget.shadow.disabled", new Color(0x66222222, true));

        /* Module Card */
        colors.put("module.name", new Color(0xCBCBCB));
        colors.put("module.description", new Color(0x7A7979));

        colors.put("module.enabled.name", new Color(0x47FF91));
        colors.put("module.enabled.shadow", new Color(0x8047FF91, true));

        colors.put("module.disabled.name", new Color(0xCBCBCB));

        colors.put("module.conflicting.name", new Color(0xFF5555));
        colors.put("module.conflicting.shadow", new Color(0x80FF5555, true));
    }

    public @Nullable Color get(String key) {
        return colors.get(key);
    }

    public Color get(String key, Color fallback) {
        return colors.getOrDefault(key, fallback);
    }

    public int getRGB(String key) {
        return this.getRGB(key, 0xFFFFFF);
    }

    public int getRGB(String key, int fallback) {
        Color color = colors.get(key);
        if (color == null) return fallback;

        return color.getRGB();
    }

    public float[] getFloats(String key) {
        return this.getFloats(key, new float[]{1, 1, 1, 1});
    }

    public float[] getFloats(String key, float[] fallback) {
        Color color = colors.get(key);
        if (color == null) return fallback;

        return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
    }
}
