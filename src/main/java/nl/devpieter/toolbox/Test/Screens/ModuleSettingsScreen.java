package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.ContentWidget;
import nl.devpieter.toolbox.Widgets.SettingWidgets.SettingGroupWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;

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
        this.addRenderable(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);

        SText text = SText.of().append(this.module.getName().getString());
        SCLabel label = new SCLabel(sidebar, 0, 324 + 8 + 16, 424, 20, text);
        label.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(label);

        this.addRenderable(sidebar);

        this.initSettingGroups(content);

//        int y = 100;
//
//        for (int i = 0; i < this.module.getSettings().size(); i++) {
//            ISetting<?> setting = this.module.getSettings().get(i);
//
//            if (setting instanceof BooleanWidgetSetting booleanSetting) {
//                BooleanSettingWidget booleanSettingWidget = new BooleanSettingWidget(500, y, 275, 200, this.module, booleanSetting);
//                content.addChild(booleanSettingWidget);
//            }
//
//            if (setting instanceof NumberWidgetSetting<?> genericSetting) {
//
//                if (genericSetting.getValue() instanceof Integer) {
//                    NumberWidgetSetting<Integer> intSetting = (NumberWidgetSetting<Integer>) genericSetting;
//                    NumberSettingWidget<Integer, NumberWidgetSetting<Integer>> intSettingWidget = new NumberSettingWidget<>(
//                            500,
//                            y,
//                            600,
//                            200,
//                            this.module,
//                            intSetting,
//                            intSetting.getMin(),
//                            intSetting.getMax()
//                    );
//
//                    intSettingWidget.setParent(content);
//                    content.addChild(intSettingWidget);
//                }
//
//                if (genericSetting.getValue() instanceof Float) {
//                    NumberWidgetSetting<Float> floatSetting = (NumberWidgetSetting<Float>) genericSetting;
//                    NumberSettingWidget<Float, NumberWidgetSetting<Float>> floatSettingWidget = new NumberSettingWidget<>(
//                            500,
//                            y,
//                            600,
//                            200,
//                            this.module,
//                            floatSetting,
//                            floatSetting.getMin(),
//                            floatSetting.getMax()
//                    );
//
//                    floatSettingWidget.setParent(content);
//                    content.addChild(floatSettingWidget);
//                }
//            }
//
//            y += 50;
    }

    private void initSettingGroups(ContentWidget content) {
        float gap = 16;

        float x = content.getX() + gap;
        float y = content.getY() + gap;

        for (int i = 0; i < this.module.getSettingGroups().size(); i++) {
            ISettingGroup settingGroup = this.module.getSettingGroups().get(i);
            SettingGroupWidget settingGroupWidget = new SettingGroupWidget(x, y, content.getWidth() / 2 - gap -gap, settingGroup);

            settingGroupWidget.setParent(content);
            content.addChild(settingGroupWidget);

            if (i % 2 == 0) {
                x = content.getX() + gap;
                y += settingGroupWidget.getHeight() + gap;
            } else {
                x += settingGroupWidget.getWidth() + gap;
            }
        }
    }
}
