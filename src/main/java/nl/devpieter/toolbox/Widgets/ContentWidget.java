package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.time.Duration;

public class ContentWidget extends WidgetBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final Animation slideAnimation;

    public ContentWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

//        Color backgroundColor = new Color(9, 9, 9, 111); // #
        Color backgroundColor = colorManager.get("content.background");

        slideAnimation = new Animation(height * 2, 0, Duration.ofMillis(250), Easing.QUINT_OUT, true);

        SCCard content = new SCCard(this, x, y, width, height);
        content.color().set(backgroundColor).cornerRadius().set(16);
        addChild(content);
    }

    @Override
    protected void render(long context, float delta) {
        NanoVG.nvgTranslate(context, 0, this.slideAnimation.get());
    }
}
