package nl.devpieter.toolbox.Theme;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.font.Font;
import nl.devpieter.squid.rendering.font.FontUtils;
import nl.devpieter.toolbox.Toolbox;

import java.io.InputStream;

public class FontManager implements SynchronousResourceReloader {

    private static FontManager INSTANCE;

    public static Font KAUSHAN_SCRIPT = new Font("kaushan-script", true,
            "/assets/toolbox/fonts/kaushan_script/KaushanScript-Regular.ttf",
            null,
            null,
            null,
            null
    );

    private FontManager() {
    }

    public static FontManager getInstance() {
        if (INSTANCE == null) INSTANCE = new FontManager();
        return INSTANCE;
    }

    @Override
    public void reload(ResourceManager manager) {
        try (InputStream inputStream = FontManager.class.getResourceAsStream("/assets/toolbox/fonts/kaushan_script/KaushanScript-Regular.ttf")) {
            if (inputStream == null) throw new Exception("Resource not found");

            if (!NVGHelper.hasContext()) {
                NVGHelper.init();
            }

            FontUtils.loadFontHolder(NVGHelper.vgContext, KAUSHAN_SCRIPT.getRegular(), inputStream);

        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to load font: Kaushan Script", e);
        }
    }
}
