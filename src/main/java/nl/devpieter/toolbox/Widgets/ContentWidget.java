package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.animation.Animation;
import nl.devpieter.squid.rendering.animation.Easing;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.time.Duration;

public class ContentWidget extends WidgetBase {

    private final Animation slideAnimation;

    public ContentWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        Color background = new Color(9, 9, 9, 111);

        slideAnimation = new Animation(height * 2, 0, Duration.ofMillis(250), Easing.QUINT_OUT, true);

        SCCard content = new SCCard(this, x, y, width, height);
        content.color().set(background).cornerRadius().set(16);
        addChild(content);
    }

    @Override
    protected void render(long context, float delta) {
        NanoVG.nvgTranslate(context, 0, this.slideAnimation.get());
    }
}
