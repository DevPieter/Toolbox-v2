package nl.devpieter.toolbox.widgets.settingWidgets;

import nl.devpieter.squid.rendering.SkiaContext;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.module.interfaces.ISettingModule;
import nl.devpieter.toolbox.widgets.WidgetBase;
import nl.devpieter.toolbox.setting.SettingGroup;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.util.ArrayList;
import java.util.List;

public class SettingGroupWidget extends WidgetBase {

    private final float gap;

    private final ISettingModule module;
    private final SettingGroup settingGroup;

    public SettingGroupWidget(float x, float y, float width, float gap, ISettingModule module, SettingGroup settingGroup) {
        super(x, y, width, 0);

        this.gap = gap;

        this.module = module;
        this.settingGroup = settingGroup;

        List<IComponent> settingComponents = this.createSettingComponents();
        this.setHeight(this.getChildrenHeight(settingComponents, gap));

        /* Header */
        SCCard cardHeader = new SCCard(this, x, y, width, 50);
        cardHeader.color().set(colorManager.get("setting.group.header")).cornerRadius().set(8, 8, 0, 0);
        this.addChild(cardHeader);

        STextStyle titleStyle = STextStyle.of(colorManager.get("setting.group.header.text"), 24).setBold();
        SCLabel title = new SCLabel(cardHeader, x + 12, y, width - 24, 40, SText.of(this.settingGroup.title(), titleStyle));
        title.alignment().set(Alignment.MIDDLE_LEFT);
        cardHeader.addChild(title);

        /* Body */
        SCCard cardBody = new SCCard(this, x, y + 40, width, height - 40);
        cardBody.color().set(colorManager.get("setting.group.body")).cornerRadius().set(12, 12, 8, 8);
        this.addChild(cardBody);

        for (IComponent settingComponent : settingComponents) {
            settingComponent.setParent(cardBody);
            cardBody.addChild(settingComponent);
        }
    }

    private List<IComponent> createSettingComponents() {
        List<IComponent> components = new ArrayList<>();
        float y = this.getY() + 40 + this.gap;

        for (int i = 0; i < this.settingGroup.settings().size(); i++) {
            IWidgetSetting<?, ?> setting = this.settingGroup.settings().get(i);

            IComponent widget = setting.createWidget(this.getX() + 12, y, this.getWidth() - 24, 250, this.module);
            components.add(widget);

            y += widget.getHeight() + this.gap;
        }

        return components;
    }

    @Override
    protected void render(SkiaContext context, float delta) {

    }

    private float getChildrenHeight(List<IComponent> children, float gap) {
        IRenderable child = children.stream().max((a, b) -> Float.compare(a.getY(), b.getY())).orElse(null);
        return child == null ? 0.0F : child.getY() + child.getHeight() - this.getY() + gap;
    }
}
