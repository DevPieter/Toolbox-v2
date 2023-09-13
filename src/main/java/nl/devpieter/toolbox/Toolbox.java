package nl.devpieter.toolbox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Screens.ModuleScreen;
import nl.devpieter.toolbox.Test.Screens.TestScreen;
import nl.devpieter.toolbox.Test.Filler.*;
import nl.devpieter.toolbox.Test.Modules.ClickableModule;
import nl.devpieter.toolbox.Test.Modules.StatefulModule;
import nl.devpieter.toolbox.Test.Modules.TestModule;
import nl.devpieter.toolbox.Test.Modules.ToggleableModule;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Toolbox implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Toolbox");
    public static boolean DEBUG = false;

    private final KeyBinding keyBinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("toggle.test", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "testing"));
    private final KeyBinding keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("open.screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "testing"));

    private final KeyBinding toggleDebug = KeyBindingHelper.registerKeyBinding(new KeyBinding("toggle.debug", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "testing"));

    @Override
    public void onInitialize() {
        ModuleManager moduleManager = ModuleManager.getInstance();

        moduleManager.register(ClickableModule.class);
        moduleManager.register(StatefulModule.class);
        moduleManager.register(TestModule.class);
        moduleManager.register(ToggleableModule.class);

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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding1.wasPressed()) {
                client.setScreen(new TestScreen());
            }
            if (keyBinding2.wasPressed()) {
                client.setScreen(new ModuleScreen());
            }

            if (toggleDebug.wasPressed()) {
                Toolbox.DEBUG = !Toolbox.DEBUG;
                Toolbox.LOGGER.info("Debug mode: " + Toolbox.DEBUG);
            }
        });
    }
}