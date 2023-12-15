package nl.devpieter.toolbox.Helpers;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class BackgroundHelper {

    private final ManagedShaderEffect blurShader = ShaderEffectManager.getInstance().manage(new Identifier("toolbox", "shaders/post/background_blur.json"));
    private final Uniform1f blurProgress = blurShader.findUniform1f("Progress");

    private final float blurRadius;
    private final int transparency;
    private final float fadeinTime;

    private long start = -1;

    public BackgroundHelper(float blurRadius, int transparency, float fadeinTime) {
        this.blurShader.findUniform1f("Radius").set(blurRadius);
        this.blurProgress.set(0);

        this.blurRadius = blurRadius;
        this.transparency = transparency;
        this.fadeinTime = fadeinTime;
    }

    public void render(MatrixStack matrices, int width, int height, float delta) {
        if (this.start <= 0) this.start = System.currentTimeMillis();

        if (this.transparency > 0) {
            DrawableHelper.fill(matrices, 0, 0, width, height, getBackgroundColor());
        }

        if (this.blurRadius > 0) {
            this.blurProgress.set(getProgress());
            this.blurShader.render(delta);
        }
    }

    public void reset() {
        this.start = -1;
    }

    private float getProgress() {
        float x = Math.min((System.currentTimeMillis() - this.start) / this.fadeinTime, 1);
        x *= (2 - x);
        return x;
    }

    private int getBackgroundColor() {
        int alpha = (int) (getProgress() * this.transparency);
        return new Color(0, 0, 0, alpha).getRGB();
    }
}
