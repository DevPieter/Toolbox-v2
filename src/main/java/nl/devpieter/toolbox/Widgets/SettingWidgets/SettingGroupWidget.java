package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;

import java.awt.*;

public class SettingGroupWidget extends ComponentBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final ISettingGroup settingGroup;

    public SettingGroupWidget(float x, float y, float width, ISettingGroup settingGroup) {
        super(x, y, width, 145); // TODO - Calculate height based on the amount of settings in the group

        this.settingGroup = settingGroup;

        Color titleColor = colorManager.get("module.card.title");
        Color topColor = colorManager.get("module.card.top");
        Color bottomColor = colorManager.get("module.card.bottom");
        Color descriptionColor = colorManager.get("module.card.description");

        SCCard cardTop = new SCCard(this, x, y, width, 50);
        cardTop.color().set(topColor).cornerRadius().set(8, 8, 0, 0);
        this.addChild(cardTop);

        SCCard cardBottom = new SCCard(this, x, y + 50, width, 50);
        cardBottom.color().set(bottomColor).cornerRadius().set(0, 0, 8, 8);
        this.addChild(cardBottom);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(24);
        SCLabel title = new SCLabel(this, x + 12, y, width - 24, 40, SText.of(titleStyle).append(settingGroup.getTitle()));
        title.alignment().set(Alignment.MIDDLE_LEFT);
        this.addChild(title);
    }

    @Override
    protected void render(long context, float delta) {

    }
}
