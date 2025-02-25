package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;
import nl.devpieter.toolbox.setting.abstractions.IWidgetSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettingGroupWidget extends ComponentBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final float gap;

    private final ISettingModule module;
    private final ISettingGroup settingGroup;

    public SettingGroupWidget(float x, float y, float width, float gap, ISettingModule module, ISettingGroup settingGroup) {
        super(x, y, width, 0); // TODO - Calculate height based on the amount of settings in the group

        this.gap = gap;

        this.module = module;
        this.settingGroup = settingGroup;

        List<IComponent> settingComponents = this.createSettingComponents();
        this.setHeight(this.getChildrenHeight(settingComponents, gap));

        Color titleColor = colorManager.get("module.card.title"); // TODO - setting.group.title
        Color topColor = colorManager.get("module.card.top");
        Color bottomColor = colorManager.get("module.card.bottom");

        SCCard cardTop = new SCCard(this, x, y, width, 50);
        cardTop.color().set(topColor).cornerRadius().set(8, 8, 0, 0);
        addChild(cardTop);

        SCCard cardBottom = new SCCard(this, x, y + 40, width, height - 40);
        cardBottom.color().set(bottomColor).cornerRadius().set(12, 12, 8, 8);
        addChild(cardBottom);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(24);
        SCLabel title = new SCLabel(this, x + 12, y, width - 24, 40, SText.of(titleStyle).append(settingGroup.title()));
        title.alignment().set(Alignment.MIDDLE_LEFT);
        addChild(title);

        for (IComponent settingComponent : settingComponents) {
            settingComponent.setParent(this);
            this.addChild(settingComponent);
        }
    }

    private List<IComponent> createSettingComponents() {
        List<IComponent> components = new ArrayList<>();
        float y = this.getY() + 40 + this.gap;

        for (int i = 0; i < this.settingGroup.settings().size(); i++) {
            IWidgetSetting<?> setting = this.settingGroup.settings().get(i);

            IComponent widget = setting.createWidget(this, this.getX() + 12, y, this.getWidth() - 24, 250, this.module);
            components.add(widget);

            y += widget.getHeight() + this.gap;
        }

        return components;
    }

    @Override
    protected void render(long context, float delta) {

    }

    private float getChildrenHeight(List<IComponent> children, float gap) {
        IRenderable child = children.stream().max((a, b) -> Float.compare(a.getY(), b.getY())).orElse(null);
        return child == null ? 0.0F : child.getY() + child.getHeight() - this.getY() + gap;
    }
}
