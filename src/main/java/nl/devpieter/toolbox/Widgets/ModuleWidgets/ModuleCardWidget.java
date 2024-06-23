package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import nl.devpieter.toolbox.Helpers.AnimationHelper;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.IInteractableModule;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Utils.TextUtils;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.lwjgl.glfw.GLFW;

import java.time.Duration;

public class ModuleCardWidget extends Widget {

    public static final int WIDTH = 160;
    public static final int HEIGHT = 50;

    private final ModuleManager moduleManager = ModuleManager.getInstance();

    private final Class<? extends IModule> moduleClass;
    private final boolean isInteractable, isClickable, isToggleable;

    /* Animation */
    private boolean wasHovering = false;
    private final AnimationHelper scaleAnimation = new AnimationHelper(0.75F, 1.0F, Duration.ofMillis(400), AnimationHelper.Easing.EXPO_IN_OUT);
    private final AnimationHelper offsetYAnimation = new AnimationHelper(0.0F, -1.4F, Duration.ofMillis(150), AnimationHelper.Easing.SINE_IN_OUT);

    public ModuleCardWidget(Class<? extends IModule> moduleClass, int x, int y) {
        super(x, y, WIDTH, HEIGHT);

        this.moduleClass = moduleClass;
        this.isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
        this.isClickable = IClickableModule.class.isAssignableFrom(moduleClass);
        this.isToggleable = IToggleableModule.class.isAssignableFrom(moduleClass);
    }

    @Override
    protected void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta) {
        boolean hovering = this.isMouseOver(relativeMouseX, relativeMouseY);

        if (this.isInteractable && this.wasHovering != hovering) {
            this.offsetYAnimation.reset();
        }

        this.wasHovering = hovering;
        this.offsetYAnimation.setForward(this.isInteractable && hovering);

        this.scale = this.scaleAnimation.get();

        // TODO - Check matrices
        this.pushMatrices(context, 0, 0);
        context.getMatrices().translate(0, this.offsetYAnimation.get(), 0);

        this.drawBackground(context, hovering);
        this.drawName(context);
        this.drawDescription(context);

        this.popMatrices(context);
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

    private void drawBackground(DrawContext context, boolean hovering) {
        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(context, 0, 0, this.getWidth(), this.getHeight(), 0, 0, 3, 4, backgroundColor, shadowColor);
    }

    private void drawName(DrawContext context) {
        Style style = Style.EMPTY.withColor(this.getNameColor());
        MutableText text = TextUtils.currentOrDefaultStyle(this.moduleManager.getName(this.moduleClass), style);

        int x = 6, y = 6;
        int scaledWidth = this.getScaledWidth() - (x * 2);

        TextUtils.drawTruncatedText(context, this.textRenderer, text, style, x, y, scaledWidth, this.getScale(), false);
    }

    private void drawDescription(DrawContext context) {
        Style style = Style.EMPTY.withColor(this.colorManager.getRGB("module.description"));
        MutableText text = TextUtils.currentOrDefaultStyle(this.moduleManager.getDescription(this.moduleClass), style);

        int x = 6, y = 20;
        int scaledWidth = this.getScaledWidth() - (x * 2) - 1;
        int scaledHeight = this.getScaledHeight() - y - 4;

        TextUtils.drawTruncatedMultilineText(context, this.textRenderer, text, style, x, y, scaledWidth, scaledHeight, this.getScale() * 0.8F, false);
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
