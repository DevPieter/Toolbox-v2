package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.ContentWidget;
import nl.devpieter.toolbox.Widgets.SettingWidgets.SettingGroupContainerWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;

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

        SettingGroupContainerWidget settingGroupContainer = new SettingGroupContainerWidget(content.getX(), content.getY(), content.getWidth(), content.getHeight(), 16);
        settingGroupContainer.init(this.module);

        settingGroupContainer.setParent(content);
        content.addChild(settingGroupContainer);

        this.addRenderable(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);

        SText text = SText.of().append(this.module.getName().getString());
        SCLabel label = new SCLabel(sidebar, 0, 324 + 8 + 16, 424, 20, text);
        label.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(label);

        this.addRenderable(sidebar);

//        this.initSettingGroups(content);

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

//    private void initSettingGroups(ContentWidget content) {
//        float gap = 16;
//
//        float minWidth = 400;
//        float maxWidth = 600;
//
//        int maxPerRow = calculateFittingCardsRow(content.getWidth(), minWidth, maxWidth, gap);
//        float cardWidth = calculateCardWidth(content.getWidth(), gap, maxPerRow);
//
//        float[] rowsHeights = new float[maxPerRow];
//
//        for (int i = 0; i < this.module.getSettingGroups().size(); i++) {
//            int row = i % maxPerRow;
//            float rowHeight = rowsHeights[row];
//
//            float x = content.getX() + row * (cardWidth + gap) + gap;
//            float y = content.getY() + rowHeight + gap /*+ (324 + 8 + 16)*/;
//
//            ISettingGroup settingGroup = this.module.getSettingGroups().get(i);
//            SettingGroupWidget settingGroupWidget = new SettingGroupWidget(x, y, cardWidth, settingGroup);
//
//            settingGroupWidget.setParent(content);
//            content.addChild(settingGroupWidget);
//
//            rowsHeights[row] += settingGroupWidget.getHeight() + gap;
//        }
//    }
//
//    // Calculate the amount of cards that fit in the horizontal direction
//    private int calculateFittingCardsRow(float width, float minWidth, float maxWidth, float gap) {
//        return Math.max(1, Math.min((int) Math.floor((width - gap) / (minWidth + gap)), (int) Math.floor((width - gap) / (maxWidth + gap))));
//    }
//
//    private float calculateCardWidth(float width, float gap, int maxPerRow) {
//        return (width - gap * (maxPerRow + 1)) / maxPerRow;
//    }
}
