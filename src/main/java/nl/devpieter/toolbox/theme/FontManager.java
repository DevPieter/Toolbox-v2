package nl.devpieter.toolbox.theme;

import io.github.humbleui.skija.FontStyle;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import nl.devpieter.squid.Squid;
import nl.devpieter.squid.rendering.font.Font;
import nl.devpieter.squid.rendering.font.FontHolder;
import nl.devpieter.toolbox.Toolbox;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Loads resource-pack fonts without requiring a graphics context. */
public final class FontManager implements ResourceManagerReloadListener, AutoCloseable {

    private static final FontManager INSTANCE = new FontManager();
    private static final Pattern FONT_PATH = Pattern.compile("font/([^/]+)/(regular|bold|italic|bold_italic|light)\\.ttf");

    private final Map<String, Font> loadedFonts = new HashMap<>();
    private final Map<String, Font> managedFonts = new HashMap<>();
    private boolean managesDefaultFont;

    private FontManager() {
    }

    public static FontManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.reload(manager);
    }

    public void reload(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.listResources("font", path ->
                path.getNamespace().equals("toolbox") && FONT_PATH.matcher(path.getPath()).matches());
        this.managedFonts.values().forEach(FontManager::closeFont);
        closeFont(Squid.DEFAULT_FONT);
        this.managesDefaultFont = true;
        this.loadedFonts.clear();

        Map<String, Font> candidates = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Matcher path = FONT_PATH.matcher(entry.getKey().getPath());
            if (!path.matches()) continue;
            String name = path.group(1);
            String variant = path.group(2);
            // Keep holder identity so styles on an open screen see reloaded fonts.
            Font font = candidates.computeIfAbsent(name, key -> key.equals("default")
                    ? Squid.DEFAULT_FONT : this.managedFonts.computeIfAbsent(key, FontManager::createFont));
            FontHolder holder = holder(font, variant);
            try (InputStream input = entry.getValue().open()) {
                if (!holder.load(input)) Toolbox.LOGGER.warn("Failed to load font {} ({})", name, variant);
            } catch (Exception e) {
                Toolbox.LOGGER.error("Failed to load font {} ({})", name, variant, e);
            }
        }

        for (Map.Entry<String, Font> entry : candidates.entrySet()) {
            if (entry.getValue().regular().isLoaded()) this.loadedFonts.put(entry.getKey(), entry.getValue());
            else {
                Toolbox.LOGGER.warn("Skipping font {} because its regular face is missing or invalid", entry.getKey());
                closeFont(entry.getValue());
            }
        }
        if (!Squid.DEFAULT_FONT.regular().isLoaded()) loadSystemDefaults(Squid.DEFAULT_FONT);
        Squid.IS_DEFAULT_FONT_LOADED = Squid.DEFAULT_FONT.regular().isLoaded();
        Toolbox.LOGGER.info("Loaded {} theme fonts", this.loadedFonts.size());
    }

    public Font get(String key) {
        return this.get(key, Squid.DEFAULT_FONT);
    }

    public Font get(String key, Font fallback) {
        return this.loadedFonts.getOrDefault(key, fallback);
    }

    @Override
    public void close() {
        this.managedFonts.values().forEach(FontManager::closeFont);
        this.managedFonts.clear();
        this.loadedFonts.clear();
        if (this.managesDefaultFont) {
            closeFont(Squid.DEFAULT_FONT);
            Squid.IS_DEFAULT_FONT_LOADED = false;
            this.managesDefaultFont = false;
        }
    }

    private static Font createFont(String name) {
        return new Font(name, new FontHolder(name + "-regular"), new FontHolder(name + "-bold"),
                new FontHolder(name + "-italic"), new FontHolder(name + "-bold-italic"), new FontHolder(name + "-light"));
    }

    private static FontHolder holder(Font font, String variant) {
        return switch (variant) {
            case "bold" -> font.bold();
            case "italic" -> font.italic();
            case "bold_italic" -> font.boldItalic();
            case "light" -> font.light();
            default -> font.regular();
        };
    }

    private static void closeFont(Font font) {
        Set<FontHolder> holders = Collections.newSetFromMap(new IdentityHashMap<>());
        Collections.addAll(holders, font.regular(), font.bold(), font.italic(), font.boldItalic(), font.light());
        holders.forEach(FontHolder::close);
    }

    private static void loadSystemDefaults(Font font) {
        font.regular().loadSystem(FontStyle.NORMAL);
        font.bold().loadSystem(FontStyle.BOLD);
        font.italic().loadSystem(FontStyle.ITALIC);
        font.boldItalic().loadSystem(FontStyle.BOLD_ITALIC);
        font.light().loadSystem(FontStyle.NORMAL.withWeight(300));
    }
}
