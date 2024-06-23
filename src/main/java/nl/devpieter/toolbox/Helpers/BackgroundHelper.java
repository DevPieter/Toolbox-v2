package nl.devpieter.toolbox.Helpers;

import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.time.Duration;

public class BackgroundHelper {

    private final AnimationHelper animationHelper = new AnimationHelper(0, 1, Duration.ofSeconds(1));

    private final float blurRadius;
    private final int transparency;

    public BackgroundHelper(float blurRadius, int transparency, Duration duration) {
        this.animationHelper.setDuration(duration);

        this.blurRadius = blurRadius;
        this.transparency = transparency;
    }

    public void render(DrawContext context, int width, int height, float delta) {
        if (this.transparency > 0) {
            context.fill(0, 0, width, height, getBackgroundColor());
        }

        // TODO - Minecraft 1.20.? adds a blur by default, we can't change the radius easily, so I will do this later.
        // GameRenderer -> renderBlur(delta); (Radius can be changed but we need to use mixins)
    }

    public void reset() {
        this.animationHelper.reset();
    }

    private int getBackgroundColor() {
        int alpha = (int) (this.transparency * this.animationHelper.get());
        return new Color(0, 0, 0, alpha).getRGB();
    }
}
