package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Test.Screens.ModuleSettingsScreen;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.settings.BooleanSetting;
import nl.devpieter.toolbox.setting.settings.ClampedFloatSetting;
import nl.devpieter.toolbox.setting.settings.ClampedIntSetting;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestSettingModule implements ISettingModule, IClickableModule {

    private final BooleanWidgetSetting isEnable = new BooleanWidgetSetting(
            "Is Enable",
            "Is this module enabled?",
            new BooleanSetting(
                    "is-enable",
                    false
            )
    );

    private final BooleanWidgetSetting playSound = new BooleanWidgetSetting(
            "Play Sound",
            "Should we play a sound?",
            new BooleanSetting(
                    "play-sound",
                    false
            )
    );

    private final BooleanWidgetSetting trackClicks = new BooleanWidgetSetting(
            "Track Clicks",
            "Should we track clicks?",
            new BooleanSetting(
                    "track-clicks",
                    false
            )
    );

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

    private final NumberWidgetSetting<Integer> middleClicks = new NumberWidgetSetting<>(
            "Middle Clicks",
            "The amount of middle clicks.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "middle-clicks",
                    0,
                    0,
                    100
            )
    );

    private final NumberWidgetSetting<Float> speed = new NumberWidgetSetting<>(
            "Speed",
            "The speed of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedFloatSetting(
                    "speed",
                    0.5f,
                    0.1f,
                    5.0f
            ),
            true, false, true
    );

    private final NumberWidgetSetting<Float> yaw = new NumberWidgetSetting<>(
            "Yaw",
            "The yaw of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedFloatSetting(
                    "yaw",
                    0.0f,
                    -180.0f,
                    180.0f
            ),
            false, false, false
    );

    private final NumberWidgetSetting<Float> pitch = new NumberWidgetSetting<>(
            "Pitch",
            "The pitch of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedFloatSetting(
                    "pitch",
                    0.0f,
                    -90.0f,
                    90.0f
            ),
            false, false, false
    );

    private final NumberWidgetSetting<Float> volume = new NumberWidgetSetting<>(
            "Volume",
            "The volume of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedFloatSetting(
                    "volume",
                    5.5f,
                    0.0f,
                    10.0f
            ),
            true, false, true
    );

    private final NumberWidgetSetting<Integer> volume2 = new NumberWidgetSetting<>(
            "Volume 2",
            "The volume of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "volume2",
                    5,
                    0,
                    10
            )
    );

    private final NumberWidgetSetting<Integer> volume3 = new NumberWidgetSetting<>(
            "Volume 3",
            "The volume of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "volume3",
                    5,
                    0,
                    10
            ),
            false, false, true
    );

    private final NumberWidgetSetting<Integer> volume4 = new NumberWidgetSetting<>(
            "Volume 4",
            "The volume of the module.",
            NumberWidgetSetting.WidgetType.SLIDER,
            new ClampedIntSetting(
                    "volume4",
                    5,
                    0,
                    10
            )
    );

    @Override
    public List<ISetting<?>> getSettings() {
        return List.of(
                isEnable.getSetting(),
                playSound.getSetting(),
                trackClicks.getSetting(),
                leftClicks.getSetting(),
                rightClicks.getSetting(),
                middleClicks.getSetting(),
                speed.getSetting(),
                yaw.getSetting(),
                pitch.getSetting(),
                volume.getSetting(),
                volume2.getSetting(),
                volume3.getSetting(),
                volume4.getSetting()
        );
    }

    @Override
    public List<SettingGroup> getSettingGroups() {
        return List.of(
                new SettingGroup("General", List.of(
                        isEnable,
                        trackClicks
                )),
                new SettingGroup("Clicks", List.of(
                        leftClicks,
                        rightClicks,
                        middleClicks
                )),
                new SettingGroup("Movement", List.of(
                        speed,
                        yaw,
                        pitch
                )),
                new SettingGroup("Sound", List.of(
                        playSound,
                        volume,
                        volume2,
                        volume3,
                        volume4
                )),
                new SettingGroup("Misc", List.of(
                        volume
                ))
//                new SettingGroup("All", List.of(
//                        isEnable,
//                        playSound,
//                        trackClicks,
//                        leftClicks,
//                        rightClicks,
//                        middleClicks,
//                        speed,
//                        yaw,
//                        pitch,
//                        volume,
//                        volume2,
//                        volume3,
//                        volume4
//                ))
        );
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
        return Text.of("Test Setting Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that has settings.");
    }
}
