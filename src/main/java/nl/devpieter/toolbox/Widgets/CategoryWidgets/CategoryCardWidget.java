package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.font.Fonts;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;

import java.awt.*;

public class CategoryCardWidget extends WidgetBase {

    public CategoryCardWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        float borderSize = 1;
        Color background = new Color(26, 27, 30);
        Color text = new Color(255, 255, 255);
        Color gray = new Color(118, 118, 118);
        Color selected = new Color(71, 255, 145);

        // TODO - This is a bit hacky, but it works for now
        SCCard border = new SCCard(this, x - borderSize, y - borderSize, width + borderSize * 2, height + borderSize * 2);
        border.setColor(gray.brighter(), true).setCornerRadius(8);
        this.addChild(border);

        SCCard card = new SCCard(this, x, y, width, height);
        card.setColor(background.brighter(), true).setCornerRadius(8);
        this.addChild(card);

        STextStyle titleStyle = STextStyle.of().setFont(Fonts.CALIBRI).setFontSize(20);
        SCLabel title = new SCLabel(this, x + 16, y + 8, width - 32, 20, SText.of(titleStyle).append("Test Category"));
        title.setColor(text.brighter(), true);
        this.addChild(title);

        STextStyle descriptionStyle = STextStyle.of().setFont(Fonts.CALIBRI).setFontSize(16);
        SCLabel description = new SCLabel(this, x + 16, y + 40, width - 32, 16, SText.of(descriptionStyle).append("This is just a test category."));
        description.setColor(gray.brighter(), true);
        this.addChild(description);
    }

    @Override
    protected void render(long l, float v) {

    }
}
