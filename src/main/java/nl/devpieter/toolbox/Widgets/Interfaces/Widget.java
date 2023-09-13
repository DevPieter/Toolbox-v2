package nl.devpieter.toolbox.Widgets.Interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Toolbox;

import java.awt.*;
import java.util.Random;

public abstract class Widget extends DrawableHelper implements Drawable, Element, Selectable {

    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected final ColorManager colorManager = ColorManager.getInstance();
    protected final TextRenderer textRenderer = client.textRenderer;

    /* ========================= Debug =========================*/
    private final Random debugRandom = new Random(this.getClass().getSimpleName().hashCode());
    private final Color debugColor = new Color(debugRandom.nextInt(255), debugRandom.nextInt(255), debugRandom.nextInt(255));
    /* =========================================================*/

    protected float x, y, width, height, scale = 1;
    protected boolean visible = true, focused = false;

    public Widget(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.render(matrices, (double) mouseX, (double) mouseY, delta);
    }

    public void render(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        if (!this.isVisible()) return;

        matrices.push();
        matrices.scale(this.scale, this.scale, 1);
        matrices.translate(this.x / this.scale, this.y / this.scale, 0);

        this.renderWidget(matrices, (mouseX - this.x) / this.scale, (mouseY - this.y) / this.scale, delta);

        if (Toolbox.DEBUG) drawBorder(matrices, 0, 0, (int) this.width, (int) this.height, debugColor.getRGB());
        matrices.pop();
    }

    protected abstract void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta);

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.getScaledWidth() && mouseY >= this.y && mouseY <= this.y + this.getScaledHeight();
    }

    public boolean isMouseOverRelative(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY, 0, 0, this.width, this.height);
    }

    public boolean isMouseOver(double mouseX, double mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getScaledWidth() {
        return this.width * this.scale;
    }

    public float getScaledHeight() {
        return this.height * this.scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
