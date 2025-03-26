package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.settings.BooleanSetting;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestSetting2Module implements ISettingModule, IClickableModule {

    private final BooleanWidgetSetting isEnable = new BooleanWidgetSetting(
            "Is Enable",
            "Is this module enabled?",
            new BooleanSetting(
                    "is-enable",
                    false
            )
    );

    @Override
    public List<ISetting<?>> getSettings() {
        return List.of(
                isEnable.getSetting()
        );
    }

    @Override
    public List<SettingGroup> getSettingGroups() {
        return List.of();
    }

    @Override
    public @NotNull Action onLeftClick() {
        return IClickableModule.super.onLeftClick();
    }

    @Override
    public @NotNull Action onMiddleClick() {
        return IClickableModule.super.onMiddleClick();
    }

    @Override
    public @NotNull Action onRightClick() {
        return IClickableModule.super.onRightClick();
    }

    @Override
    public @NotNull Text getName() {
        return Text.of("Test Setting Module 2");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that has settings.");
    }
}
