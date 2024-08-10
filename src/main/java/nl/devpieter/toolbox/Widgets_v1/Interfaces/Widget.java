package nl.devpieter.toolbox.Widgets_v1.Interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Theme.CardManager;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public abstract class Widget implements Drawable, Element, Selectable {

    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected final CardManager cardManager = CardManager.getInstance();
    protected final ColorManager colorManager = ColorManager.getInstance();
    protected final TextRenderer textRenderer = client.textRenderer;

    /* ========================= Debug =========================*/
    private final Random debugRandom = new Random(this.getClass().getSimpleName().hashCode());
    private final Color debugColor = new Color(debugRandom.nextInt(255), debugRandom.nextInt(255), debugRandom.nextInt(255));
    /* =========================================================*/

    protected int x, y, width, height;
    protected float scale = 1f;

    protected boolean visible = true, focused = false;

    protected abstract void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta);

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isVisible()) return;
        this.pushMatrices(context, this.getX(), this.getY());

        // TODO - Does not 100% work as intended when scaled
        int relativeMouseX = (int) (mouseX / this.getScale() - this.getX());
        int relativeMouseY = (int) (mouseY / this.getScale() - this.getY());

        this.renderWidget(context, relativeMouseX, relativeMouseY, delta);

        if (Toolbox.DEBUG) {
            context.drawBorder(0, 0, this.getScaledWidth(), this.getScaledHeight(), this.debugColor.getRGB());
        }

        this.popMatrices(context);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getScaledWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getScaledHeight();
    }

    public boolean isMouseOver(int relativeMouseX, int relativeMouseY) {
        return relativeMouseX >= 0 && relativeMouseX <= this.getScaledWidth() && relativeMouseY >= 0 && relativeMouseY <= this.getScaledHeight();
    }

    public void pushMatrices(@NotNull DrawContext context, int x, int y) {
        this.pushMatrices(context, x, y, 1);
    }

    public void pushMatrices(@NotNull DrawContext context, int x, int y, float scale) {
        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.scale(this.getScale() * scale, this.getScale() * scale, 1);
        matrices.translate(x / (this.getScale() * scale), y / (this.getScale() * scale), 0);
    }

    public void popMatrices(@NotNull DrawContext context) {
        context.getMatrices().pop();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getScaledWidth() {
        return (int) (this.getWidth() * this.getScale());
    }

    public int getHeight() {
        return height;
    }

    public int getScaledHeight() {
        return (int) (this.getHeight() * this.getScale());
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
