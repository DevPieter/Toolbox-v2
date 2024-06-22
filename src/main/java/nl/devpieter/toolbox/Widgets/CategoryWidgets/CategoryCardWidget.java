package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Callbacks.ICallback;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Utils.SoundUtils;
import nl.devpieter.toolbox.Utils.TextUtils;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class CategoryCardWidget extends Widget {

    public static final int HEIGHT = 30;

    private final CategoryManager categoryManager = CategoryManager.getInstance();

    private final @Nullable Class<? extends ICategory> categoryClass;
    private final ICallback<@Nullable Class<? extends ICategory>> callback;

    private boolean selected = false;

    public CategoryCardWidget(@Nullable Class<? extends ICategory> categoryClass, int x, int y, int width, ICallback<@Nullable Class<? extends ICategory>> callback) {
        super(x, y, width, HEIGHT);

        this.categoryClass = categoryClass;
        this.callback = callback;
    }

    @Override
    protected void renderWidget(MatrixStack matrices, int relativeMouseX, int relativeMouseY, float delta) {
        boolean hovering = this.isMouseOver(relativeMouseX, relativeMouseY);

        this.drawBackground(matrices, hovering);
        this.drawName(matrices);
        this.drawDescription(matrices);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || !this.isMouseOver(mouseX, mouseY)) return false;
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return false;

        SoundEvent soundEvent = SoundEvents.ENTITY_ITEM_PICKUP;
        if (soundEvent != null) SoundUtils.playOnMaster(soundEvent, 1.0F, 1.0F);

        this.callback.invoke(this.categoryClass);
        return true;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public @Nullable Class<? extends ICategory> getCategoryClass() {
        return this.categoryClass;
    }

    private void drawBackground(@NotNull MatrixStack matrices, boolean hovering) {
        this.pushMatrices(matrices, 0, 0);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(matrices, 0, 0, this.getWidth(), this.getHeight(), 0, 0, 3, 4, backgroundColor, shadowColor);
        this.popMatrices(matrices);
    }

    private void drawName(@NotNull MatrixStack matrices) {
        Style style = Style.EMPTY.withColor(this.getNameColor());
        MutableText text = TextUtils.currentOrDefaultStyle(this.getName(), style);

        int x = 6, y = 6;
        int scaledWidth = this.getScaledWidth() - (x * 2);

        TextUtils.drawTruncatedText(matrices, this.textRenderer, text, style, x, y, scaledWidth, this.getScale(), false);
    }

    private void drawDescription(@NotNull MatrixStack matrices) {
        Style style = Style.EMPTY.withColor(this.colorManager.getRGB("module.description"));
        MutableText text = TextUtils.currentOrDefaultStyle(this.getDescription(), style);

        int x = 6, y = 20;
        int scaledWidth = this.getScaledWidth() - (x * 2);

        TextUtils.drawTruncatedText(matrices, this.textRenderer, text, style, x, y, scaledWidth, this.getScale() * 0.8F, false);
    }

    private Text getName() {
        if (this.categoryClass == null) return Text.of("All");
        return this.categoryManager.getName(this.categoryClass);
    }

    private Text getDescription() {
        if (this.categoryClass == null) return Text.of("All the modules you love.");
        return this.categoryManager.getDescription(this.categoryClass);
    }

    private float[] getShadowColor(boolean hovering) {
        if (this.selected) return this.colorManager.getFloats("module.enabled.shadow");
        if (hovering) return this.colorManager.getFloats("widget.shadow.hover");
        return this.colorManager.getFloats("widget.shadow");
    }

    private float[] getBackgroundColor(boolean hovering) {
        if (hovering) return this.colorManager.getFloats("widget.background.hover");
        return this.colorManager.getFloats("widget.background");
    }

    private int getNameColor() {
        if (this.selected) return this.colorManager.getRGB("module.enabled.name");
        return this.colorManager.getRGB("module.name");
    }
}
