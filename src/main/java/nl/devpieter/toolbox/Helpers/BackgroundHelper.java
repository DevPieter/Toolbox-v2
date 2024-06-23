package nl.devpieter.toolbox.Helpers;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.time.Duration;

public class BackgroundHelper {

    private final ManagedShaderEffect blurShader = ShaderEffectManager.getInstance().manage(new Identifier("toolbox", "shaders/post/background_blur.json"));
    private final Uniform1f blurProgress = blurShader.findUniform1f("Progress");

    private final AnimationHelper animationHelper = new AnimationHelper(0, 1, Duration.ofSeconds(1));

    private final float blurRadius;
    private final int transparency;

    public BackgroundHelper(float blurRadius, int transparency, Duration duration) {
        this.blurShader.findUniform1f("Radius").set(blurRadius);
        this.blurProgress.set(0);

        this.animationHelper.setDuration(duration);

        this.blurRadius = blurRadius;
        this.transparency = transparency;
    }

    public void render(MatrixStack matrices, int width, int height, float delta) {
        if (this.transparency > 0) {
            DrawableHelper.fill(matrices, 0, 0, width, height, getBackgroundColor());
        }

        if (this.blurRadius > 0) {
            this.blurProgress.set(this.animationHelper.get());
            this.blurShader.render(delta);
        }
    }

    public void reset() {
        this.animationHelper.reset();
    }

    private int getBackgroundColor() {
        int alpha = (int) (this.transparency * this.animationHelper.get());
        return new Color(0, 0, 0, alpha).getRGB();
    }
}
