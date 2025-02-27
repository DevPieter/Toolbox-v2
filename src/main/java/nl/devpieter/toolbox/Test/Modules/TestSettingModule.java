package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Test.Screens.ModuleSettingsScreen;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.settings.BooleanSetting;
import nl.devpieter.toolbox.setting.settings.ClampedIntSetting;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestSettingModule implements ISettingModule, IClickableModule {

    // String name, String description, WidgetType type, IClampedSetting<T> setting

    private final NumberWidgetSetting<Integer> leftClicks = new NumberWidgetSetting<>(
            "Left Clicks",
            "The amount of left clicks.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "left-clicks",
                    0,
                    0,
                    100
            )
    );

    private final NumberWidgetSetting<Integer> rightClicks = new NumberWidgetSetting<>(
            "Right Clicks",
            "The amount of right clicks.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "right-clicks",
                    0,
                    0,
                    100
            )
    );

    private final BooleanWidgetSetting isEnable = new BooleanWidgetSetting(
            "Is Enable",
            "Is this module enabled?",
            new BooleanSetting(
                    "is-enable",
                    false
            )
    );

    private final BooleanWidgetSetting myBoolean = new BooleanWidgetSetting(
            "My Boolean",
            "This is a test boolean setting.",
            new BooleanSetting(
                    "my-boolean",
                    false
            )
    );

    @Override
    public List<ISetting<?>> getSettings() {
        return List.of(
                leftClicks.getSetting(),
                rightClicks.getSetting(),
                isEnable.getSetting(),
                myBoolean.getSetting()
        );
    }

    @Override
    public List<SettingGroup> getSettingGroups() {
        return List.of(
                new SettingGroup("Clicks", List.of(leftClicks, rightClicks)),
                new SettingGroup("Settings", List.of(leftClicks, rightClicks, isEnable)),
                new SettingGroup("Test", List.of(myBoolean)),
                new SettingGroup("Movement", List.of(leftClicks, rightClicks)),
                new SettingGroup("Misc", List.of(isEnable))
        );
    }

    @Override
    public @NotNull Action onLeftClick() {
//        SettingManager.getInstance().saveSetting(this, leftClicks);

        MinecraftClient.getInstance().setScreen(new ModuleSettingsScreen(this));

        return IClickableModule.super.onLeftClick();
    }

    @Override
    public @NotNull Action onMiddleClick() {
//        leftClicks.setValue(0);
//        rightClicks.setValue(0);

        SettingManager.getInstance().saveSettings(this);
        return IClickableModule.super.onMiddleClick();
    }

    @Override
    public @NotNull Action onRightClick() {
//        rightClicks.increment();

//        SettingManager.getInstance().saveSetting(this, rightClicks);
        return IClickableModule.super.onRightClick();
    }

    @Override
    public @NotNull Text getName() {
        return Text.of("Test Setting Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that has settings.");
    }
}
