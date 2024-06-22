package nl.devpieter.toolbox.Module;

import com.google.gson.Gson;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Utils.ConfigUtils;
import nl.devpieter.toolbox.Module.Interfaces.*;
import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    private final Gson gson = new Gson();
    private final List<IModule> modules = new ArrayList<>();

    private ModuleManager() {
    }

    private @Nullable IModule getInstance(@NotNull Class<? extends IModule> moduleClass) {
        return this.modules.stream().filter(module -> module.getClass().equals(moduleClass)).findFirst().orElse(null);
    }

    public void register(@NotNull Class<? extends IModule> moduleClass) {
        if (this.isRegistered(moduleClass)) {
            Toolbox.LOGGER.warn("Module ({}) is already registered!", moduleClass.getSimpleName());
            return;
        }

        try {
            IModule module = moduleClass.getConstructor().newInstance();

            this.modules.add(module);
            if (module instanceof IStatefulModule<?>) this.loadState(moduleClass);

            module.onRegistered();
        } catch (Exception e) {
            Toolbox.LOGGER.error("Module ({}) could not be registered! Make sure the module has a public no-args constructor.", moduleClass.getSimpleName(), e);
        }
    }

    public void unregister(@NotNull Class<? extends IModule> moduleClass) {
        IModule module = this.getInstance(moduleClass);
        if (module == null) {
            Toolbox.LOGGER.warn("Module ({}) is not registered!", moduleClass.getSimpleName());
            return;
        }

        if (module instanceof IStatefulModule<?>) this.saveState(moduleClass);
        this.modules.remove(module);

        module.onUnregistered();
    }

    public List<Class<? extends IModule>> getModules() {
        return List.copyOf(this.modules.stream().map(IModule::getClass).toList());
    }

    public List<Class<? extends IModule>> getModules(@Nullable Class<? extends ICategory> categoryClass) {
        if (categoryClass == null) return this.getModules();
        return List.copyOf(this.modules.stream().filter(module -> module.getCategories().contains(categoryClass)).map(IModule::getClass).toList());
    }

    public boolean isRegistered(@NotNull Class<? extends IModule> moduleClass) {
        return this.modules.stream().anyMatch(module -> module.getClass().equals(moduleClass));
    }

    public Text getName(@NotNull Class<? extends IModule> moduleClass) {
        IModule module = this.getInstance(moduleClass);
        return module == null ? Text.of("Unknown") : module.getName();
    }

    public Text getDescription(@NotNull Class<? extends IModule> moduleClass) {
        IModule module = this.getInstance(moduleClass);
        return module == null ? Text.of("Unknown") : module.getDescription();
    }

    public boolean click(@NotNull Class<? extends IModule> moduleClass, int button) {
        if (!(this.getInstance(moduleClass) instanceof IClickableModule clickableModule)) return false;
        clickableModule.onClick(button);

        IInteractableModule.Action action = switch (button) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> clickableModule.onLeftClick();
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> clickableModule.onMiddleClick();
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> clickableModule.onRightClick();
            default -> IInteractableModule.Action.CONTINUE;
        };

        IInteractableModule.playSound(clickableModule, action, button);
        return action.name().startsWith("CANCEL");
    }

    public void loadState(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return;

        try {
            File moduleFile = ConfigUtils.getModuleFile(statefulModule, false);
            if (moduleFile == null || statefulModule.getState() == null) return;

            statefulModule.setState(this.gson.fromJson(new FileReader(moduleFile), (Type) statefulModule.getState().getClass()));
        } catch (Exception e) {
            Toolbox.LOGGER.warn("Failed to load state for module {}!", moduleClass.getSimpleName(), e);
            statefulModule.resetState();
        }
    }

    public void saveState(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return;

        try {
            File moduleFile = ConfigUtils.getModuleFile(statefulModule, true);
            if (moduleFile == null) return;

            try (FileWriter writer = new FileWriter(moduleFile)) {
                this.gson.toJson(statefulModule.getState(), writer);
            }
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to save state for module {}!", moduleClass.getSimpleName(), e);
        }
    }

    public void resetState(@NotNull Class<? extends IModule> moduleClass, boolean save) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return;
        statefulModule.resetState();
        if (save) this.saveState(moduleClass);
    }

    public boolean isEnabled(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof IToggleableModule toggleableModule)) return false;
        return toggleableModule.isEnabled();
    }

    public void setEnabled(@NotNull Class<? extends IModule> moduleClass, boolean enabled) {
        if (!(this.getInstance(moduleClass) instanceof IToggleableModule toggleableModule)) return;
        IInteractableModule.Action action;

        if (enabled) {
            action = toggleableModule.onEnable();
            if (action.name().startsWith("CANCEL")) return;

            toggleableModule.setEnabled(true);
            toggleableModule.onEnabled();
        } else {
            action = toggleableModule.onDisable();
            if (action.name().startsWith("CANCEL")) return;

            toggleableModule.setEnabled(false);
            toggleableModule.onDisabled();
        }

        IInteractableModule.playSound(toggleableModule, action, -1);
    }

    public void toggle(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof IToggleableModule toggleableModule)) return;
        this.setEnabled(moduleClass, !toggleableModule.isEnabled());
    }
}
