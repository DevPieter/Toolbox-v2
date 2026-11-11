package nl.devpieter.toolbox.theme;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import nl.devpieter.squid.rendering.svg.SvgHolder;
import nl.devpieter.toolbox.Toolbox;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class SvgManager implements ResourceManagerReloadListener, AutoCloseable {

    private static final SvgManager INSTANCE = new SvgManager();
    private final Map<String, SvgHolder> loadedSvgs = new HashMap<>();
    private final Map<String, SvgHolder> managedSvgs = new HashMap<>();

    private SvgManager() {
    }

    public static SvgManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.reload(manager);
    }

    public void reload(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.listResources("svg", path ->
                path.getNamespace().equals("toolbox") && path.getPath().endsWith(".svg"));
        this.managedSvgs.values().forEach(SvgHolder::close);
        this.loadedSvgs.clear();

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            String path = entry.getKey().getPath();
            String name = path.substring(4, path.length() - 4).replace('/', '.');
            SvgHolder holder = this.managedSvgs.computeIfAbsent(name, SvgHolder::new);
            try (InputStream input = entry.getValue().open()) {
                if (holder.load(input)) this.loadedSvgs.put(name, holder);
                else holder.close();
            } catch (Exception e) {
                holder.close();
                Toolbox.LOGGER.error("Failed to load SVG {}", name, e);
            }
        }
        Toolbox.LOGGER.info("Loaded {} theme SVGs", this.loadedSvgs.size());
    }

    public SvgHolder get(String key) {
        return this.loadedSvgs.get(key);
    }

    public SvgHolder getOrDefault(String key, SvgHolder fallback) {
        return this.loadedSvgs.getOrDefault(key, fallback);
    }

    @Override
    public void close() {
        this.managedSvgs.values().forEach(SvgHolder::close);
        this.managedSvgs.clear();
        this.loadedSvgs.clear();
    }
}
