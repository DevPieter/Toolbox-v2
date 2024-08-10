package nl.devpieter.toolbox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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
import nl.devpieter.toolbox.Test.Screens.TestScreen;
import nl.devpieter.toolbox.Test.Screens.TestSquidScreen;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Toolbox implements ModInitializer {

    public static final String MOD_VERSION = "2.x.x";
    public static final String MOD_STATUS = "Pre-Alpha";
    public static final String GAME_VERSION = "1.20.6";

    public static final Logger LOGGER = LoggerFactory.getLogger("Toolbox");
    public static boolean DEBUG = false;

    public static final Font KAUSHAN_SCRIPT = new Font("kaushan-script", false,
            "D:\\DEVELOPMENT\\Minecraft-Coding\\Mods\\Fabric\\Toolbox\\KaushanScript-Regular.ttf",
            null,
            null,
            null,
            null
    );

    private final KeyBinding keyBinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("toolbox.toggle.test", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "testing"));
    private final KeyBinding keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("toolbox.open.screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "testing"));

    private final KeyBinding toggleDebug = KeyBindingHelper.registerKeyBinding(new KeyBinding("toolbox.toggle.debug", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "testing"));

    @Override
    public void onInitialize() {

        CategoryManager categoryManager = CategoryManager.getInstance();
        categoryManager.register(MiscCategory.class);
        categoryManager.register(ToolsCategory.class);

        ModuleManager moduleManager = ModuleManager.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding1.wasPressed()) {
                client.setScreen(new TestSquidScreen());
            }
            if (keyBinding2.wasPressed()) {
                client.setScreen(new TestScreen());
            }

            if (toggleDebug.wasPressed()) {
                Toolbox.DEBUG = !Toolbox.DEBUG;
                Toolbox.LOGGER.info("Debug mode: " + Toolbox.DEBUG);
            }
        });

        /* DEBUG / TESTING*/
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