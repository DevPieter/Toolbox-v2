package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.toolbox.Theme.ColorManager;

import java.awt.*;
import java.time.Duration;

public class ContentWidget extends ComponentBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final Animation slideAnimation;

    public ContentWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        Color backgroundColor = colorManager.get("content.background");

        this.slideAnimation = new Animation(height * 2, 0, Duration.ofMillis(250), Easing.QUINT_OUT, true);

        SCCard content = new SCCard(this, x, y, width, height);
        content.color().set(backgroundColor).cornerRadius().set(16);
        this.addChild(content);
    }

    @Override
    protected void render(long context, float delta) {
        NVGHelper.translate(context, 0, this.slideAnimation.get());
    }
}
