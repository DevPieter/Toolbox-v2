package nl.devpieter.toolbox.Widgets.Interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Theme.ColorManager;

public abstract class Widget extends DrawableHelper implements Drawable, Element, Selectable {

    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected final ColorManager colorManager = ColorManager.getInstance();
    protected final TextRenderer textRenderer = client.textRenderer;


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

        /* ========================= Debug =========================*/
//        Random random = new Random(this.getClass().getSimpleName().hashCode());
//        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
//        drawBorder(matrices, 0, 0, (int) this.width, (int) this.height, color.getRGB());
        /* =========================================================*/

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
        return mouseX >= this.x && mouseX <= this.x + this.getScaleWidth() && mouseY >= this.y && mouseY <= this.y + this.getScaleHeight();
    }

    public boolean isMouseOverRelative(double mouseX, double mouseY) {
        return mouseX >= 0 && mouseX <= width && mouseY >= 0 && mouseY <= height;
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

    public float getScaleWidth() {
        return this.width * this.scale;
    }

    public float getScaleHeight() {
        return this.height * this.scale;
    }

    public float getScale() {
        return scale;
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
}
