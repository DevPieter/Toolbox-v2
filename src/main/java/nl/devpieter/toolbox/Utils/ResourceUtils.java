package nl.devpieter.toolbox.Utils;

import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URL;

public class ResourceUtils {

    public static @Nullable File getResourceFolder(String path) {

//        ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
//        resourcePackManager.scanPacks();
//
//        for (ResourcePack pack : resourcePackManager.createResourcePacks()) {
//            for(String namespace : pack.getNamespaces(ResourceType.CLIENT_RESOURCES)) {
//
//                pack.findResources(ResourceType.CLIENT_RESOURCES, namespace, path, (identifier, inputStreamSupplier) -> {
//                    Toolbox.LOGGER.info("=== Found resource: " + identifier);
//                });
//            }
//        }

        URL resource = ResourceUtils.class.getClassLoader().getResource("assets/toolbox/" + path);
        if (resource == null) {
            Toolbox.LOGGER.error("Failed to find resource folder!");
            return null;
        }

        try {
            return new File(resource.toURI());
        } catch (Exception e) {
            try {
                return new File(resource.getPath());
            } catch (Exception e2) {
                Toolbox.LOGGER.error("Failed to find resource folder!", e2);
                return null;
            }
        }
    }
}
