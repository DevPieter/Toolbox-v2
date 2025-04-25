package nl.devpieter.toolbox.module;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import nl.devpieter.toolbox.GlobalSettings;
import nl.devpieter.toolbox.Test.Screens.ModuleSettingsScreen;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.category.ICategory;
import nl.devpieter.toolbox.module.interfaces.*;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.settings.ListSetting;
import nl.devpieter.toolbox.utils.ConfigUtils;
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

    private static ModuleManager INSTANCE;

    private final Gson gson = new Gson();
    private final List<IModule> modules = new ArrayList<>();

    private final SettingManager settingManager = SettingManager.getInstance();
    private final GlobalSettings globalSettings = GlobalSettings.getInstance();

    private ModuleManager() {
    }

    public static ModuleManager getInstance() {
        if (INSTANCE == null) INSTANCE = new ModuleManager();
        return INSTANCE;
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
            if (module instanceof ISettingModule) this.loadSettings(moduleClass);

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
        if (module instanceof ISettingModule) this.saveSettings(moduleClass);
        this.modules.remove(module);

        module.onUnregistered();
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends IModule>> getModules() {
        ListSetting<String> setting = this.globalSettings.getSetting("favorite_modules", ListSetting.class);
        if (setting == null) return List.copyOf(this.modules.stream().map(IModule::getClass).toList());

        List<Class<? extends IModule>> favoriteModules = new ArrayList<>();
        List<Class<? extends IModule>> otherModules = new ArrayList<>();

        for (IModule module : this.modules) {
            if (setting.contains(this.getIdentifier(module.getClass()))) favoriteModules.add(module.getClass());
            else otherModules.add(module.getClass());
        }

        favoriteModules.addAll(otherModules);
        return List.copyOf(favoriteModules);
    }

    public List<Class<? extends IModule>> getModules(@Nullable Class<? extends ICategory> categoryClass) {
        if (categoryClass == null) return this.getModules();
        return List.copyOf(this.modules.stream()
                .filter(module -> module.getCategories().contains(categoryClass))
                .map(IModule::getClass)
                .toList()
        );
    }

//    public List<Class<? extends IModule>> getModules(@Nullable Class<? extends ICategory> categoryClass, String query) {
//        if (query == null || query.isEmpty()) return this.getModules(categoryClass);
//
//        return List.copyOf(this.getModules(categoryClass).stream()
//                .filter(moduleClass -> {
//                    IModule module = this.getInstance(moduleClass);
//                    return module != null && (module.getName().toLowerCase().contains(query) ||
//                            module.searchTerms().stream().anyMatch(term -> term.toLowerCase().contains(query)) ||
//                            module.getDescription().toLowerCase().contains(query)
//                    );
//                }).toList()
//        );
//    }

    public boolean isRegistered(@NotNull Class<? extends IModule> moduleClass) {
        return this.modules.stream().anyMatch(module -> module.getClass().equals(moduleClass));
    }

    public String getIdentifier(@NotNull Class<? extends IModule> moduleClass) {
        return moduleClass.getPackageName() + "." + moduleClass.getSimpleName();
    }

    public String getName(@NotNull Class<? extends IModule> moduleClass) {
        IModule module = this.getInstance(moduleClass);
        return module != null ? module.getName() : "Unknown";
    }

    public String getDescription(@NotNull Class<? extends IModule> moduleClass) {
        IModule module = this.getInstance(moduleClass);
        return module != null ? module.getDescription() : "Unknown";
    }

    @SuppressWarnings("unchecked")
    public boolean isFavorite(@NotNull Class<? extends IModule> moduleClass) {
        ListSetting<String> setting = this.globalSettings.getSetting("favorite_modules", ListSetting.class);
        return setting != null && setting.contains(this.getIdentifier(moduleClass));
    }

    @SuppressWarnings("unchecked")
    public boolean setFavorite(@NotNull Class<? extends IModule> moduleClass, boolean favorite) {
        ListSetting<String> setting = this.globalSettings.getSetting("favorite_modules", ListSetting.class);
        if (setting == null) return false;

        if (favorite) setting.add(this.getIdentifier(moduleClass));
        else setting.remove(this.getIdentifier(moduleClass));

        File settingsFile = ConfigUtils.getSettingsFile(true);
        if (settingsFile == null) return false;

        return this.settingManager.queueSave(settingsFile, setting);
    }

    public boolean toggleFavorite(@NotNull Class<? extends IModule> moduleClass) {
        return this.setFavorite(moduleClass, !this.isFavorite(moduleClass));
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
            File stateFile = ConfigUtils.getModuleStateFile(statefulModule, false);
            if (stateFile == null || statefulModule.getState() == null) return;

            statefulModule.setState(this.gson.fromJson(new FileReader(stateFile), (Type) statefulModule.getState().getClass()));
        } catch (Exception e) {
            Toolbox.LOGGER.warn("Failed to load state for module {}!", moduleClass.getSimpleName(), e);
            statefulModule.resetState();
        }
    }

    public void saveState(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return;

        try {
            File stateFile = ConfigUtils.getModuleStateFile(statefulModule, true);
            if (stateFile == null) return;

            try (FileWriter writer = new FileWriter(stateFile)) {
                this.gson.toJson(statefulModule.getState(), writer);
            }
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to save state for module {}!", moduleClass.getSimpleName(), e);
        }
    }

    public <T> @Nullable T getState(@NotNull Class<? extends IModule> moduleClass, Class<T> stateClass) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return null;

        try {
            Object state = statefulModule.getState();
            if (!stateClass.isInstance(state)) return null;

            return stateClass.cast(state);
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to get state for module {}!", moduleClass.getSimpleName(), e);
            return null;
        }
    }

    public void resetState(@NotNull Class<? extends IModule> moduleClass, boolean save) {
        if (!(this.getInstance(moduleClass) instanceof IStatefulModule<?> statefulModule)) return;

        statefulModule.resetState();
        if (save) this.saveState(moduleClass);
    }

    public boolean loadSettings(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof ISettingModule settingModule)) return false;

        File settingsFile = ConfigUtils.getModuleSettingsFile(settingModule, false);
        if (settingsFile == null) return false;

        return this.settingManager.loadSettings(settingsFile, settingModule.getSettings());
    }

    public boolean saveSettings(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof ISettingModule settingModule)) return false;

        File settingsFile = ConfigUtils.getModuleSettingsFile(settingModule, true);
        if (settingsFile == null) return false;

        return this.settingManager.queueSave(settingsFile, settingModule.getSettings());
    }

    public boolean openSettings(@NotNull Class<? extends IModule> moduleClass) {
        if (!(this.getInstance(moduleClass) instanceof ISettingModule settingModule)) return false;
        MinecraftClient.getInstance().setScreen(new ModuleSettingsScreen(settingModule));
        return true;
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

    private @Nullable IModule getInstance(@NotNull Class<? extends IModule> moduleClass) {
        return this.modules.stream().filter(module -> module.getClass().equals(moduleClass)).findFirst().orElse(null);
    }
}
