package nl.devpieter.toolbox.Widgets_old;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import nl.devpieter.toolbox.Utils.TextUtils;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.IInteractableModule;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Widgets_old.Interfaces.Widget;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ModuleCardWidget extends Widget {

    // The raw background texture is 400x125 pixels, but we scale it down to 160x50 pixels.
    public static final float SCALE_DOWN_FACTOR = 400F / 160F;
    public static final float WIDTH = 400 / SCALE_DOWN_FACTOR, HEIGHT = 125 / SCALE_DOWN_FACTOR;

    private final Identifier background = new Identifier("toolbox", "textures/gui/module_card/background.png");
    private final Identifier backgroundShadow = new Identifier("toolbox", "textures/gui/module_card/background_shadow.png");

    private final ModuleManager moduleManager = ModuleManager.getInstance();

    private final Class<? extends IModule> moduleClass;
    private final boolean isInteractable;

    /* Animation */
    private float offsetY = 0;

    public ModuleCardWidget(Class<? extends IModule> moduleClass, float x, float y) {
        super(x, y, WIDTH, HEIGHT);

        this.moduleClass = moduleClass;
        this.isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
    }

    @Override
    protected void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        if (this.isInteractable) {
            float blend = MathHelper.clamp(0.8F * delta, 0.0F, 1.0F);
            this.offsetY = MathHelper.lerp(blend, this.offsetY, this.isMouseOverRelative(mouseX, mouseY) ? -1.4F : 0);
        }

        this.renderBackground(matrices, mouseX, mouseY);
        this.renderName(matrices, 8, 6 + this.offsetY);
        this.renderDescription(matrices, 8, 20 + this.offsetY, 0.75F, 4);
    }

    private void renderBackground(MatrixStack matrices, double mouseX, double mouseY) {
        matrices.push();
        matrices.scale(1.0F / SCALE_DOWN_FACTOR, 1.0F / SCALE_DOWN_FACTOR, 1);

        /* Init */
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        /* Shadow */
        float[] shadowColor = this.getShadowColor(mouseX, mouseY);
        RenderSystem.setShaderColor(shadowColor[0], shadowColor[1], shadowColor[2], shadowColor[3]);

        RenderSystem.setShaderTexture(0, this.backgroundShadow);
        drawTexture(matrices, -12, -12 + (int) ((this.offsetY * SCALE_DOWN_FACTOR) / 1.6F), 0, 0, 432, 157, 432, 157);

        /* Background */
        float[] bgColor = this.getBackgroundColor(mouseX, mouseY);
        RenderSystem.setShaderColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        RenderSystem.setShaderTexture(0, this.background);
        drawTexture(matrices, 0, (int) (this.offsetY * SCALE_DOWN_FACTOR), 0, 0, 400, 125, 400, 125);

        /* Reset */
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        matrices.pop();
    }

    private void renderName(MatrixStack matrices, float x, float y) {
        matrices.push();
        matrices.translate(x, y, 0);

        /* Init */
        Style style = Style.EMPTY.withColor(this.getNameColor());
        MutableText text = TextUtils.currentOrDefaultStyle(this.moduleManager.getName(this.moduleClass), style);

        /* Calculate */
        int maxWidth = MathHelper.floor((this.getWidth() - (x * 2))) - 3; // 3 = width of the dots / 2
        List<OrderedText> lines = this.textRenderer.wrapLines(text, maxWidth);

        /* Draw */
        OrderedText finalText = lines.size() > 1 ? OrderedText.concat(lines.get(0), TextUtils.withStyle("...", style).asOrderedText()) : lines.get(0);
        this.textRenderer.draw(matrices, finalText, 0, 0, 0xFFFFFF);

        matrices.pop();
    }

    private void renderDescription(MatrixStack matrices, float x, float y, float scale, float paddingY) {
        matrices.push();
        matrices.scale(scale, scale, 1);
        matrices.translate(x / scale, y / scale, 0);

        /* Init */
        Style style = Style.EMPTY.withColor(this.getDescriptionColor());
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
        matrices.pop();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || !this.isMouseOver(mouseX, mouseY)) return false;

        if (IClickableModule.class.isAssignableFrom(this.moduleClass)) {
            if (this.moduleManager.click(this.moduleClass, button)) return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && IToggleableModule.class.isAssignableFrom(this.moduleClass)) {
            this.moduleManager.toggle(this.moduleClass);
            return true;
        }

        return false;
    }

    private float[] getBackgroundColor(double mouseX, double mouseY) {
        if (this.isConflicting()) {
            return this.colorManager.getFloats("widget.background.disabled");
        }

        if (this.isInteractable && this.isMouseOverRelative(mouseX, mouseY)) {
            return this.colorManager.getFloats("widget.background.hover");
        }

        return this.colorManager.getFloats("widget.background");
    }

    private float[] getShadowColor(double mouseX, double mouseY) {
        if (this.moduleManager.isEnabled(this.moduleClass)) {
            return this.colorManager.getFloats("module.enabled.shadow");
        }

        if (this.isConflicting()) {
            return this.colorManager.getFloats("module.conflicting.shadow");
        }

        if (this.isInteractable && this.isMouseOverRelative(mouseX, mouseY)) {
            return this.colorManager.getFloats("widget.shadow.hover");
        }

        return this.colorManager.getFloats("widget.shadow");
    }

    private int getNameColor() {
        if (this.moduleManager.isEnabled(this.moduleClass)) {
            return this.colorManager.getRGB("module.enabled.name");
        }

        if (this.isConflicting()) {
            return this.colorManager.getRGB("module.conflicting.name");
        }

        if (IToggleableModule.class.isAssignableFrom(this.moduleClass)) {
            return this.colorManager.getRGB("module.disabled.name");
        }

        return this.colorManager.getRGB("module.name");
    }

    private int getDescriptionColor() {
        return this.colorManager.getRGB("module.description");
    }

    private boolean isConflicting() {
        return false; // TODO - Conflicting modules
    }
}
