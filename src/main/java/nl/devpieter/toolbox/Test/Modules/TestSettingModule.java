package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Test.Screens.ModuleSettingsScreen;
import nl.devpieter.toolbox.setting.ISetting;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.settings.IntSetting;
import nl.devpieter.toolbox.setting.settings.ListSetting;
import nl.devpieter.toolbox.setting.settings.StringSetting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestSettingModule implements ISettingModule, IClickableModule {

    private final StringSetting myString = new StringSetting("my-string", "Hello, World!");
    private final StringSetting myString2 = new StringSetting("my-string-2", "This is a test setting!");

    private final IntSetting leftClicks = new IntSetting("left-clicks", 0);
    private final IntSetting rightClicks = new IntSetting("right-clicks", 0);

    private final ListSetting<String> myList = new ListSetting<>("my-list", List.of("Hello", "World", "This", "Is", "A", "List"));

    @Override
    public List<ISetting<?>> getSettings() {
        return List.of(
                myString,
                myString2,
                leftClicks,
                rightClicks,
                myList
        );
    }

    @Override
    public @NotNull Action onLeftClick() {
        leftClicks.increment();
        SettingManager.getInstance().saveSetting(this, leftClicks);

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
        rightClicks.increment();

        SettingManager.getInstance().saveSetting(this, rightClicks);
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
