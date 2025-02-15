package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.setting.abstractions.ISetting;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettingGroupWidget extends ComponentBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final ISettingModule module;
    private final ISettingGroup settingGroup;

    public SettingGroupWidget(float x, float y, float width, float gap, ISettingModule module, ISettingGroup settingGroup) {
        super(x, y, width, 0); // TODO - Calculate height based on the amount of settings in the group

        this.module = module;
        this.settingGroup = settingGroup;

        List<ComponentBase> settingComponents = this.createSettingComponents();
        float settingsHeight = this.getChildrenHeight(settingComponents);

        this.setHeight(settingsHeight + 10);

        Color titleColor = colorManager.get("module.card.title");
        Color topColor = colorManager.get("module.card.top");
        Color bottomColor = colorManager.get("module.card.bottom");
        Color descriptionColor = colorManager.get("module.card.description");

        SCCard cardTop = new SCCard(this, x, y, width, 50);
        cardTop.color().set(topColor).cornerRadius().set(8, 8, 0, 0);
        this.addChild(cardTop);

        SCCard cardBottom = new SCCard(this, x, y + 50, width, height - 50);
        cardBottom.color().set(bottomColor).cornerRadius().set(0, 0, 8, 8);
        this.addChild(cardBottom);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(24);
        SCLabel title = new SCLabel(this, x + 12, y, width - 24, 40, SText.of(titleStyle).append(settingGroup.title()));
        title.alignment().set(Alignment.MIDDLE_LEFT);
        this.addChild(title);

        for (ComponentBase settingComponent : settingComponents) {
            settingComponent.setParent(this);
            this.addChild(settingComponent);
        }
    }

    // TODO - THIS IS FOR TESTING PURPOSES ONLY
    private List<ComponentBase> createSettingComponents() {
        float y = this.getY() + 40 + 20;

        List<ComponentBase> components = new ArrayList<>();

        for (int i = 0; i < this.settingGroup.settings().size(); i++) {
            ISetting<?> setting = this.settingGroup.settings().get(i);

            if (setting instanceof BooleanWidgetSetting booleanSetting) {
                BooleanSettingWidget booleanSettingWidget = new BooleanSettingWidget(this.getX() + 10, y, this.getWidth() - 20, 200, this.module, booleanSetting);
                components.add(booleanSettingWidget);
            }

            if (setting instanceof NumberWidgetSetting<?> genericSetting) {

                if (genericSetting.getValue() instanceof Integer) {
                    NumberWidgetSetting<Integer> intSetting = (NumberWidgetSetting<Integer>) genericSetting;
                    NumberSettingWidget<Integer, NumberWidgetSetting<Integer>> intSettingWidget = new NumberSettingWidget<>(
                            this.getX() + 10,
                            y,
                            this.getWidth() - 20,
                            200,
                            this.module,
                            intSetting,
                            intSetting.getMin(),
                            intSetting.getMax()
                    );

                    components.add(intSettingWidget);
                }

                if (genericSetting.getValue() instanceof Float) {
                    NumberWidgetSetting<Float> floatSetting = (NumberWidgetSetting<Float>) genericSetting;
                    NumberSettingWidget<Float, NumberWidgetSetting<Float>> floatSettingWidget = new NumberSettingWidget<>(
                            this.getX() + 10,
                            y,
                            this.getWidth() - 20,
                            200,
                            this.module,
                            floatSetting,
                            floatSetting.getMin(),
                            floatSetting.getMax()
                    );

                    components.add(floatSettingWidget);
                }
            }

            y += 35;
        }

        return components;
    }

    @Override
    protected void render(long context, float delta) {

    }

    private float getChildrenHeight(List<ComponentBase> children) {
        IRenderable child = children.stream().max((a, b) -> Float.compare(a.getY(), b.getY())).orElse(null);
        return child == null ? 0.0F : child.getY() + child.getHeight() - this.getY();
    }
}
