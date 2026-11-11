package nl.devpieter.toolbox;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import nl.devpieter.squid.debug.DebugOptions;
import nl.devpieter.toolbox.Test.Screens.ModulesScreen;
import nl.devpieter.toolbox.module.development.UiTestModules;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.settings.ListSetting;
import nl.devpieter.toolbox.theme.ColorManager;
import nl.devpieter.toolbox.theme.FontManager;
import nl.devpieter.toolbox.theme.SvgManager;
import nl.devpieter.toolbox.utils.ConfigUtils;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class Toolbox implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Toolbox");
    public static boolean DEBUG = false;

    private boolean showStartupTestScreen;

    @Override
    public void onInitializeClient() {
        SettingManager settingManager = SettingManager.getInstance();
        GlobalSettings globalSettings = GlobalSettings.getInstance();
        globalSettings.addSetting(new ListSetting<String>("favorite_modules", new ArrayList<>()));

        // Register before the initial resource reload so theme assets are ready for all screens.
        ResourceLoader resources = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        resources.registerReloadListener(Identifier.fromNamespaceAndPath("toolbox", "theme/colors"), ColorManager.getInstance());
        resources.registerReloadListener(Identifier.fromNamespaceAndPath("toolbox", "theme/fonts"), FontManager.getInstance());
        resources.registerReloadListener(Identifier.fromNamespaceAndPath("toolbox", "theme/svg"), SvgManager.getInstance());

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            File settingsFile = ConfigUtils.getSettingsFile(false);
            if (settingsFile != null) settingManager.loadSettings(settingsFile, globalSettings.getSettings());
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            try {
                File settingsFile = ConfigUtils.getSettingsFile(true);
                if (settingsFile != null) settingManager.queueSave(settingsFile, globalSettings.getSettings());
                settingManager.forceSaveQueue();
            } finally {
                FontManager.getInstance().close();
                SvgManager.getInstance().close();
            }
        });

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("toolbox", "toolbox"));
        KeyMapping openModulesScreen = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "toolbox.key.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));
        KeyMapping toggleDebug = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "toolbox.key.debug", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), category));

        boolean runningAsDev = FabricLoader.getInstance().isDevelopmentEnvironment();
        this.showStartupTestScreen = runningAsDev && Boolean.getBoolean("toolbox.testScreen");
        if (this.showStartupTestScreen) UiTestModules.register();
        LOGGER.info("Running as dev: {}", runningAsDev);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            settingManager.tick();
            if (this.showStartupTestScreen && client.gui.screen() instanceof TitleScreen && client.gui.overlay() == null) {
                this.showStartupTestScreen = false;
                client.gui.setScreen(new ModulesScreen());
            }
            while (openModulesScreen.consumeClick()) client.gui.setScreen(new ModulesScreen());
            while (toggleDebug.consumeClick()) {
                DEBUG = !DEBUG;
                DebugOptions.getInstance().setDebugEnabled(DEBUG);
                LOGGER.info("Debug mode: {}", DEBUG);
            }
        });
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().name();
    }

    public static String getToolboxVersion() {
        return getModVersion("toolbox");
    }

    public static String getSquidVersion() {
        return getModVersion("squid");
    }

    private static String getModVersion(String id) {
        return FabricLoader.getInstance().getModContainer(id)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("development");
    }

    public static String getBuildStatus() {
        return FabricLoader.getInstance().getModContainer("toolbox").map(container -> {
            var status = container.getMetadata().getCustomValue("build_status");
            return status == null ? "development" : status.getAsString();
        }).orElse("development");
    }
}
