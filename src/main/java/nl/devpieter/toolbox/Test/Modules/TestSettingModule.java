package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Test.Screens.ModuleSettingsScreen;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.abstractions.ISetting;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestSettingModule implements ISettingModule, IClickableModule {

    private final NumberWidgetSetting<Integer> leftClicks = new NumberWidgetSetting<>(
            "left-clicks",
            0,
            "Left Clicks",
            "The amount of left clicks.",
            Integer.class,
            0,
            100
    );

    private final NumberWidgetSetting<Integer> rightClicks = new NumberWidgetSetting<>(
            "right-clicks",
            0,
            "Right Clicks",
            "The amount of right clicks.",
            Integer.class,
            0,
            100
    );

    private final BooleanWidgetSetting isEnable = new BooleanWidgetSetting(
            "is-enable",
            false,
            "Is Enable",
            "Is this module enabled?"
    );

    private final BooleanWidgetSetting myBoolean = new BooleanWidgetSetting(
            "my-boolean",
            false,
            "My Boolean Setting",
            "This is a test boolean setting."
    );

    @Override
    public List<ISetting<?>> getSettings() {
        return List.of(
                leftClicks,
                rightClicks,
                isEnable,
                myBoolean
        );
    }

    @Override
    public List<ISettingGroup> getSettingGroups() {
        return List.of(
                new SettingGroup("Clicks", List.of(leftClicks, rightClicks)),
                new SettingGroup("Settings", List.of(isEnable, myBoolean))
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
        leftClicks.setValue(0);
        rightClicks.setValue(0);

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
        return Text.of("Test Setting Module: " + this.leftClicks.getValue() + ", " + this.rightClicks.getValue());
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that has settings.");
    }
}
