package nl.devpieter.toolbox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import nl.devpieter.squid.debug.DebugOptions;
import nl.devpieter.squid.rendering.font.Font;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Defaults.Categories.MiscCategory;
import nl.devpieter.toolbox.Defaults.Categories.ToolsCategory;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Test.Categories.DevPieterCategory;
import nl.devpieter.toolbox.Test.Categories.ExampleCategory;
import nl.devpieter.toolbox.Test.Categories.FillerCategory;
import nl.devpieter.toolbox.Test.Filler.*;
import nl.devpieter.toolbox.Test.Modules.*;
import nl.devpieter.toolbox.Test.Screens.TestSquidScreen;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Toolbox implements ModInitializer {

    private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("toolbox").orElseThrow();

    public static final Logger LOGGER = LoggerFactory.getLogger("Toolbox");
    public static boolean DEBUG = false;

    public static Font KAUSHAN_SCRIPT;

    private final KeyBinding openModulesScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "toolbox.key.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "toolbox.key.categories.toolbox"
    ));

    private final KeyBinding toggleDebug = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "toolbox.key.debug",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "toolbox.key.categories.toolbox"
    ));

    public Toolbox() {
        KAUSHAN_SCRIPT = new Font("kaushan-script", false,
//            "D:\\DEVELOPMENT\\Minecraft-Coding\\Mods\\Fabric\\Toolbox\\KaushanScript-Regular.ttf",
                System.getProperty("user.home") + "\\AppData\\Local\\Microsoft\\Windows\\Fonts\\KaushanScript-Regular.ttf",
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onInitialize() {

        CategoryManager categoryManager = CategoryManager.getInstance();
        ModuleManager moduleManager = ModuleManager.getInstance();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            if (!(MinecraftClient.getInstance().getResourceManager() instanceof ReloadableResourceManagerImpl resourceManager)) return;
            ColorManager colorManager = ColorManager.getInstance();

            resourceManager.registerReloader(colorManager);
            colorManager.reload(resourceManager);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openModulesScreen.wasPressed()) {
                client.setScreen(new TestSquidScreen());
            }

            if (toggleDebug.wasPressed()) {
                Toolbox.DEBUG = !Toolbox.DEBUG;
                DebugOptions.getInstance().setDebug(Toolbox.DEBUG);
                Toolbox.LOGGER.info("Debug mode: " + Toolbox.DEBUG);
            }
        });

        /* DEBUG / TESTING */
        boolean runningAsDev = FabricLoader.getInstance().isDevelopmentEnvironment();
        Toolbox.LOGGER.info("Running as dev: " + runningAsDev);

        if (runningAsDev) {
            categoryManager.register(MiscCategory.class);
            categoryManager.register(ToolsCategory.class);

            categoryManager.register(DevPieterCategory.class);
            categoryManager.register(ExampleCategory.class);
            categoryManager.register(FillerCategory.class);

            moduleManager.register(ClickableModule.class);
            moduleManager.register(StatefulModule.class);
            moduleManager.register(TestModule.class);
            moduleManager.register(ToggleableModule.class);
            moduleManager.register(AutoElytraModule.class);
            moduleManager.register(BetterToggleSprintModule.class);

            // Filler modules
            moduleManager.register(FillerModule1.class);
            moduleManager.register(FillerModule2.class);
            moduleManager.register(FillerModule3.class);
            moduleManager.register(FillerModule4.class);
            moduleManager.register(FillerModule5.class);
            moduleManager.register(FillerModule6.class);
            moduleManager.register(FillerModule7.class);
            moduleManager.register(FillerModule8.class);
            moduleManager.register(FillerModule9.class);
            moduleManager.register(FillerModule10.class);
            moduleManager.register(FillerModule11.class);
            moduleManager.register(FillerModule12.class);
            moduleManager.register(FillerModule13.class);
            moduleManager.register(FillerModule14.class);
            moduleManager.register(FillerModule15.class);
        }
    }

    public static String getToolboxVersion() {
        return MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();
    }

    public static String getBuildStatus() {
        return MOD_CONTAINER.getMetadata().getCustomValue("build_status").getAsString();
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getGameVersion().getName();
    }
}