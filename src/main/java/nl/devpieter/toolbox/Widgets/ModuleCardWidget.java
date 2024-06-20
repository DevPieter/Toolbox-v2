package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.math.MathHelper;
import nl.devpieter.toolbox.Helpers.AnimationHelper;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.IInteractableModule;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Utils.TextUtils;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.time.Duration;
import java.util.List;

public class ModuleCardWidget extends Widget {

    private final ModuleManager moduleManager = ModuleManager.getInstance();

    private final Class<? extends IModule> moduleClass;
    private final boolean isInteractable, isClickable, isToggleable;

    /* Animation */
    private boolean wasHovering = false;
    private final AnimationHelper scaleAnimation = new AnimationHelper(0.75F, 1.0F, Duration.ofMillis(400), AnimationHelper.Easing.EXPO_IN_OUT);
    private final AnimationHelper offsetYAnimation = new AnimationHelper(0.0F, -1.4F, Duration.ofMillis(150), AnimationHelper.Easing.SINE_IN_OUT);

    public ModuleCardWidget(Class<? extends IModule> moduleClass, int x, int y) {
        super(x, y, 160, 50);

        this.moduleClass = moduleClass;
        this.isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
        this.isClickable = IClickableModule.class.isAssignableFrom(moduleClass);
        this.isToggleable = IToggleableModule.class.isAssignableFrom(moduleClass);
    }

    @Override
    protected void renderWidget(MatrixStack matrices, int relativeMouseX, int relativeMouseY, float delta) {
        boolean hovering = this.isMouseOver(relativeMouseX, relativeMouseY);

        if (this.isInteractable && this.wasHovering != hovering) {
            this.offsetYAnimation.reset();
        }

        this.wasHovering = hovering;
        this.offsetYAnimation.setForward(this.isInteractable && hovering);

        this.scale = this.scaleAnimation.get();
        float offsetY = this.offsetYAnimation.get();

        this.drawBackground(matrices, 0, offsetY, hovering);
        this.drawName(matrices, 6, 6 + offsetY);
        this.drawDescription(matrices, 6, 20 + offsetY, 0.65F, 4);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || !this.isMouseOver(mouseX, mouseY)) return false;

        if (this.isClickable) {
            if (this.moduleManager.click(this.moduleClass, button)) return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && this.isToggleable) {
            this.moduleManager.toggle(this.moduleClass);
            return true;
        }

        return false;
    }

    private void drawBackground(@NotNull MatrixStack matrices, float x, float y, boolean hovering) {
        this.pushMatrices(matrices, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(matrices, 0, 0, this.width, this.height, 6, 6, 3, 4, backgroundColor, shadowColor);
        this.popMatrices(matrices);
    }

    private void drawName(@NotNull MatrixStack matrices, float x, float y) {
        this.pushMatrices(matrices, x, y);

        /* Init */
        Style style = Style.EMPTY.withColor(this.getNameColor());
        MutableText text = TextUtils.currentOrDefaultStyle(this.moduleManager.getName(this.moduleClass), style);

        /* Calculate */
        int maxWidth = MathHelper.floor((this.getWidth() - (x * 2))) - 3; // 3 = width of the dots / 2
        List<OrderedText> lines = this.textRenderer.wrapLines(text, maxWidth);

        /* Draw */
        OrderedText finalText = lines.size() > 1 ? OrderedText.concat(lines.get(0), TextUtils.withStyle("...", style).asOrderedText()) : lines.get(0);
        this.textRenderer.draw(matrices, finalText, 0, 0, 0xFFFFFF);

        this.popMatrices(matrices);
    }

    private void drawDescription(@NotNull MatrixStack matrices, float x, float y, float scale, float paddingY) {
        this.pushMatrices(matrices, x, y, scale);

        /* Init */
        Style style = Style.EMPTY.withColor(this.colorManager.getRGB("module.description"));
        MutableText text = TextUtils.currentOrDefaultStyle(this.moduleManager.getDescription(this.moduleClass), style);

        /* Calculate */
        float maxWidth = (this.getWidth() - (x * 2)) / scale - 2; // 2 = some extra space for the dots <3
        float maxHeight = (this.getHeight() - (y + paddingY)) / scale;
        float lineHeight = this.textRenderer.fontHeight * 1.1F;

        List<OrderedText> lines = this.textRenderer.wrapLines(text, MathHelper.ceil(maxWidth));
        int maxLines = MathHelper.clamp(MathHelper.floor(maxHeight / lineHeight), 0, lines.size());

        /* Draw */
        for (int i = 0; i < maxLines; i++) {

            // Draw the dots if the text is too long
            if (i == maxLines - 1 && maxLines != lines.size()) {
                MutableText dots = TextUtils.withStyle("...", style);
                OrderedText finalText = OrderedText.concat(lines.get(i), dots.asOrderedText());

                this.textRenderer.draw(matrices, finalText, 0, (i * lineHeight), 0xFFFFFF);
                break;
            }

            this.textRenderer.draw(matrices, lines.get(i), 0, (i * lineHeight), 0xFFFFFF);
        }

        this.popMatrices(matrices);
    }

    private float[] getShadowColor(boolean hovering) {
        if (this.moduleManager.isEnabled(this.moduleClass)) {
            return this.colorManager.getFloats("module.enabled.shadow");
        }

//        TODO - Conflicting

        if (this.isInteractable && hovering) {
            return this.colorManager.getFloats("widget.shadow.hover");
        }

        return this.colorManager.getFloats("widget.shadow");
    }

    private float[] getBackgroundColor(boolean hovering) {
//        TODO - Conflicting

        if (this.isInteractable && hovering) {
            return this.colorManager.getFloats("widget.background.hover");
        }

        return this.colorManager.getFloats("widget.background");
    }

    private int getNameColor() {
        if (this.moduleManager.isEnabled(this.moduleClass)) {
            return this.colorManager.getRGB("module.enabled.name");
        }

//        TODO - Conflicting

        if (this.isToggleable) {
            return this.colorManager.getRGB("module.disabled.name");
        }

        return this.colorManager.getRGB("module.name");
    }
}
