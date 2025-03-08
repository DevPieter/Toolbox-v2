package nl.devpieter.toolbox.Theme;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import nl.devpieter.squid.rendering.RenderingManager;
import nl.devpieter.squid.rendering.svg.SvgHolder;
import nl.devpieter.toolbox.Toolbox;

import java.util.HashMap;
import java.util.Map;

public class SvgManager implements SynchronousResourceReloader {

    private static SvgManager INSTANCE;

    private final RenderingManager renderingManager = RenderingManager.getInstance();

    private final HashMap<String, SvgHolder> loadedSvgs = new HashMap<>();

    private SvgManager() {
    }

    public static SvgManager getInstance() {
        if (INSTANCE == null) INSTANCE = new SvgManager();
        return INSTANCE;
    }

    @Override
    public void reload(ResourceManager manager) {
        this.loadedSvgs.clear();
        this.renderingManager.init();
        long rContext = this.renderingManager.getContext();

        Map<Identifier, Resource> map = manager.findResources("svg", (path) -> {
            if (!path.getNamespace().equals("toolbox")) return false;
            return path.getPath().endsWith(".svg");
        });

        for (Map.Entry<Identifier, Resource> entry : map.entrySet()) {
            String svgPath = entry.getKey().getPath();

            // remove "svg/" and ".svg"
            String svgName = svgPath.substring(4, svgPath.length() - 4);
            svgName = svgName.replace("/", ".");

            try {
                SvgHolder svgHolder = new SvgHolder(svgName);
                svgHolder.load(rContext, entry.getValue().getInputStream());

                this.loadedSvgs.put(svgName, svgHolder);
            } catch (Exception e) {
                Toolbox.LOGGER.error("Failed to load svg: {}", svgName, e);
            }
        }
    }

    public SvgHolder get(String key) {
        return this.loadedSvgs.get(key);
    }

    public SvgHolder getOrDefault(String key, SvgHolder fallback) {
        return this.loadedSvgs.getOrDefault(key, fallback);
    }
}
