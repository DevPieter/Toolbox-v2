package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.rendering.NVGHelper;

import java.time.Duration;

public class ContentWidget extends WidgetBase {

    private final Animation slideAnimation;

    public ContentWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.slideAnimation = new Animation(height * 2, 0, Duration.ofMillis(250), Easing.QUINT_OUT, true);

        SCCard content = new SCCard(this, x, y, width, height);
        content.color().set(colorManager.get("content.body")).cornerRadius().set(16);
        this.addChild(content);
    }

    @Override
    protected void render(long context, float delta) {
        NVGHelper.translate(context, 0, this.slideAnimation.get());
    }
}
