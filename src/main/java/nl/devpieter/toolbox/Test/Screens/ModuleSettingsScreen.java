package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.ContentWidget;
import nl.devpieter.toolbox.Widgets.SettingWidgets.BooleanSettingWidget;
import nl.devpieter.toolbox.Widgets.SettingWidgets.NumberSettingWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;
import nl.devpieter.toolbox.setting.abstractions.ISetting;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;

public class ModuleSettingsScreen extends SquidScreen {

    private final ISettingModule module;

    public ModuleSettingsScreen(ISettingModule module) {
        super(Text.of("Module Settings"));
        this.module = module;
    }

    @Override
    protected void init() {
        super.init();

        ContentWidget content = new ContentWidget(424 + 16, 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 32);
        this.addWidget(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);

        SText text = SText.of().append(this.module.getName().getString());
        SCLabel label = new SCLabel(sidebar, 0, 324 + 8 + 16, 424, 20, text);
        label.setAlignment(Alignment.MIDDLE_CENTER);
        sidebar.addChild(label);

        this.addWidget(sidebar);

        int y = 100;

        for (int i = 0; i < this.module.getSettings().size(); i++) {
            ISetting<?> setting = this.module.getSettings().get(i);

            if (setting instanceof BooleanWidgetSetting booleanSetting) {
                BooleanSettingWidget booleanSettingWidget = new BooleanSettingWidget(500, y, 275, 200, this.module, booleanSetting);
                content.addChild(booleanSettingWidget);
            }

            if (setting instanceof NumberWidgetSetting<?> genericSetting) {

                if (genericSetting.getValue() instanceof Integer) {
                    NumberWidgetSetting<Integer> intSetting = (NumberWidgetSetting<Integer>) genericSetting;
                    NumberSettingWidget<Integer, NumberWidgetSetting<Integer>> intSettingWidget = new NumberSettingWidget<>(
                            500,
                            y,
                            600,
                            200,
                            this.module,
                            intSetting,
                            intSetting.getMin(),
                            intSetting.getMax()
                    );
                    content.addChild(intSettingWidget);
                }

                if (genericSetting.getValue() instanceof Float) {
                    NumberWidgetSetting<Float> floatSetting = (NumberWidgetSetting<Float>) genericSetting;
                    NumberSettingWidget<Float, NumberWidgetSetting<Float>> floatSettingWidget = new NumberSettingWidget<>(
                            500,
                            y,
                            600,
                            200,
                            this.module,
                            floatSetting,
                            floatSetting.getMin(),
                            floatSetting.getMax()
                    );
                    content.addChild(floatSettingWidget);
                }
            }

            y += 50;
        }
    }
}
