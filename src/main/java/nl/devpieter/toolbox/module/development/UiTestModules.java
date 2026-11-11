package nl.devpieter.toolbox.module.development;

import net.fabricmc.loader.api.FabricLoader;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.category.CategoryManager;
import nl.devpieter.toolbox.category.ICategory;
import nl.devpieter.toolbox.module.ModuleManager;
import nl.devpieter.toolbox.module.interfaces.*;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.settings.BooleanSetting;
import nl.devpieter.toolbox.setting.settings.ClampedIntSetting;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Fixtures are registered exclusively by the isolated development UI test run.
 */
public final class UiTestModules {

    private UiTestModules() {
    }

    public static void register() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment() || !Boolean.getBoolean("toolbox.testScreen")) return;

        CategoryManager.getInstance().register(ShowcaseCategory.class);
        CategoryManager.getInstance().register(LoremCategory.class);

        ModuleManager manager = ModuleManager.getInstance();
        manager.register(PreviewModule.class);
        manager.register(ClickModule.class);
        manager.register(InformationModule.class);
        manager.register(LoremModule.class);
    }

    public static class ShowcaseCategory implements ICategory {

        @Override
        public @NotNull String getName() {
            return "Showcase";
        }

        @Override
        public @NotNull String getDescription() {
            return "Development modules for checking Toolbox controls.";
        }
    }

    public static class LoremCategory implements ICategory {

        @Override
        public @NotNull String getName() {
            return "Lorem Ipsum";
        }

        @Override
        public @NotNull String getDescription() {
            return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        }
    }

    public static class PreviewModule implements IToggleableModule, ISettingModule, IStatefulModule<Boolean> {

        private boolean enabled;
        private final BooleanSetting notifications = new BooleanSetting("notifications", true);
        private final ClampedIntSetting intensity = new ClampedIntSetting("intensity", 65, 0, 100);

        @Override
        public @NotNull String getName() {
            return "Preview Module";
        }

        @Override
        public @NotNull String getDescription() {
            return "Toggle this module, mark it as a favorite, or open its settings.";
        }

        @Override
        public @NotNull List<Class<? extends ICategory>> getCategories() {
            return List.of(ShowcaseCategory.class);
        }

        @Override
        public boolean isEnabled() {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void onEnabled() {
            this.saveState();
        }

        @Override
        public void onDisabled() {
            this.saveState();
        }

        @Override
        public Boolean getState() {
            return this.enabled;
        }

        @Override
        public void setState(@Nullable Boolean state) {
            this.enabled = Boolean.TRUE.equals(state);
        }

        @Override
        public List<ISetting<?>> getSettings() {
            return List.of(this.notifications, this.intensity);
        }

        @Override
        public List<SettingGroup> getSettingGroups() {
            return List.of(new SettingGroup("Preview Settings", List.of(
                    new BooleanWidgetSetting("Notifications", "Show notifications for this module.", this.notifications),
                    new NumberWidgetSetting<>("Intensity", "Choose a value between 0 and 100.",
                            NumberWidgetSetting.WidgetType.SLIDER, this.intensity))));
        }
    }

    public static class ClickModule implements IClickableModule {

        @Override
        public @NotNull String getName() {
            return "Click Action";
        }

        @Override
        public @NotNull String getDescription() {
            return "Click to play the normal module interaction sound and log the action.";
        }

        @Override
        public @NotNull List<Class<? extends ICategory>> getCategories() {
            return List.of(ShowcaseCategory.class);
        }

        @Override
        public void onClick(int button) {
            Toolbox.LOGGER.info("UI test module clicked with mouse button {}", button);
        }
    }

    public static class InformationModule implements IModule {

        @Override
        public @NotNull String getName() {
            return "Information";
        }

        @Override
        public @NotNull String getDescription() {
            return "A read-only module card with wrapped text. Resize the window to inspect the layout.";
        }

        @Override
        public @NotNull List<Class<? extends ICategory>> getCategories() {
            return List.of(ShowcaseCategory.class);
        }
    }

    public static class LoremModule implements IModule {

        @Override
        public @NotNull String getName() {
            return "Lorem Ipsum";
        }

        @Override
        public @NotNull String getDescription() {
            return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        }

        @Override
        public @NotNull List<Class<? extends ICategory>> getCategories() {
            return List.of(LoremCategory.class);
        }
    }
}
