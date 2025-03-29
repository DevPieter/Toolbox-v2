package nl.devpieter.toolbox.theme;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import nl.devpieter.squid.Squid;
import nl.devpieter.squid.rendering.RenderingManager;
import nl.devpieter.squid.rendering.font.Font;
import nl.devpieter.squid.rendering.font.FontHolder;
import nl.devpieter.toolbox.Toolbox;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager implements SynchronousResourceReloader {

    private static FontManager INSTANCE;

    private final RenderingManager renderingManager = RenderingManager.getInstance();

    private final HashMap<String, Font> loadedFonts = new HashMap<>();

    private FontManager() {
    }

    public static FontManager getInstance() {
        if (INSTANCE == null) INSTANCE = new FontManager();
        return INSTANCE;
    }

    @Override
    public void reload(ResourceManager manager) {
        this.loadedFonts.clear();
        this.renderingManager.init();
        long rContext = this.renderingManager.getContext();

        Map<Identifier, Resource> map = manager.findResources("font", (path) -> {
            if (!path.getNamespace().equals("toolbox")) return false;
            return path.getPath().endsWith(".ttf");
        });

        HashMap<String, HashMap<String, FontHolder>> fontHolders = new HashMap<>();

        for (Map.Entry<Identifier, Resource> entry : map.entrySet()) {
            String fontName = entry.getKey().getPath().split("/")[1];
            String fontType = entry.getKey().getPath().split("/")[2].replace(".ttf", "");


            try (InputStream inputStream = entry.getValue().getInputStream()) {
                FontHolder fontHolder = new FontHolder(fontName + "-" + fontType);
                fontHolder.load(rContext, inputStream);

                if (!fontHolders.containsKey(fontName)) fontHolders.put(fontName, new HashMap<>());
                fontHolders.get(fontName).put(fontType, fontHolder);

                if (fontName.equals("default")) Squid.IS_DEFAULT_FONT_LOADED = true;

            } catch (Exception e) {
                Toolbox.LOGGER.error("Failed to load font holder: {}, {}", fontName, fontType, e);
            }
        }

        for (Map.Entry<String, HashMap<String, FontHolder>> entry : fontHolders.entrySet()) {
            String fontName = entry.getKey();
            HashMap<String, FontHolder> fontHolderMap = entry.getValue();

            Font font = new Font(fontName,
                    fontHolderMap.get("regular"),
                    fontHolderMap.get("bold"),
                    fontHolderMap.get("italic"),
                    fontHolderMap.get("bold_italic"),
                    fontHolderMap.get("light")
            );

            this.loadedFonts.put(fontName, font);
        }

        Toolbox.LOGGER.info("Loaded {} fonts", this.loadedFonts.size());
    }

    public Font get(String key) {
        return this.get(key, Squid.DEFAULT_FONT);
    }

    public Font get(String key, Font fallback) {
        return this.loadedFonts.getOrDefault(key, fallback);
    }
}
