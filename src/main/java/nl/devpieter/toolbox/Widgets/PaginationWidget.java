package nl.devpieter.toolbox.Widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import nl.devpieter.toolbox.Widgets.Interfaces.PaginatableWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.lwjgl.glfw.GLFW;

public class PaginationWidget extends Widget {

    public static final float SCALE_DOWN_FACTOR = ModuleCardWidget.SCALE_DOWN_FACTOR;
    public static final float WIDTH = ModuleCardWidget.WIDTH, HEIGHT = 45 / SCALE_DOWN_FACTOR;
    public static final int SECTION_WIDTH = 136, SECTION_HEIGHT = 45;

    private final Identifier background = new Identifier("toolbox", "textures/gui/pagination/background.png");
    private final Identifier backgroundShadow = new Identifier("toolbox", "textures/gui/pagination/background_shadow_todo-new.png"); // TODO - New background shadow

    private final PaginatableWidget paginatableWidget;

    public PaginationWidget(PaginatableWidget paginatableWidget, float x, float y) {
        super(x, y, WIDTH, HEIGHT);

        this.paginatableWidget = paginatableWidget;
    }

    @Override
    protected void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        this.renderPreviousButton(matrices, mouseX, mouseY);
        this.renderPageIndicator(matrices);
        this.renderNextButton(matrices, mouseX, mouseY);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double relativeMouseX = (mouseX - this.x) / this.scale;
        double relativeMouseY = (mouseY - this.y) / this.scale;

        if (this.isMouseOverPreviousButton(relativeMouseX, relativeMouseY) && this.paginatableWidget.hasPreviousPage()) {
            this.paginatableWidget.navigateToPrevious();
            return true;
        }

        if (this.isMouseOverNextButton(relativeMouseX, relativeMouseY) && this.paginatableWidget.hasNextPage()) {
            this.paginatableWidget.navigateToNext();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT && this.paginatableWidget.hasPreviousPage()) {
            this.paginatableWidget.navigateToPrevious();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT && this.paginatableWidget.hasNextPage()) {
            this.paginatableWidget.navigateToNext();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_HOME && this.paginatableWidget.hasPage(0)) {
            this.paginatableWidget.navigateToFirst();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_END && this.paginatableWidget.hasPage(this.paginatableWidget.getPageCount() - 1)) {
            this.paginatableWidget.navigateToLast();
            return true;
        }

        return false;
    }

    private void renderPreviousButton(MatrixStack matrices, double mouseX, double mouseY) {
        this.initRender(matrices);

        /* Shadow */
        // TODO - Shadow

        /* Background */
        float[] bgColor = this.getBackgroundColor(this.isMouseOverPreviousButton(mouseX, mouseY), !this.paginatableWidget.hasPreviousPage());
        RenderSystem.setShaderColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        RenderSystem.setShaderTexture(0, this.background);
        drawTexture(matrices, 0, 0, 0, 0, SECTION_WIDTH, SECTION_HEIGHT, 136, 137);

        /* Re-init */
        this.resetRender(matrices);
        this.initRender(matrices, 0.75F);

        /* Text */
        String text = "Previous";

        float sectionWidth = SECTION_WIDTH / SCALE_DOWN_FACTOR / 0.75F;
        float sectionHeight = SECTION_HEIGHT / SCALE_DOWN_FACTOR / 0.75F;

        float textX = (sectionWidth - this.textRenderer.getWidth(text)) / 2;
        float textY = (sectionHeight - this.textRenderer.fontHeight) / 2;

        this.textRenderer.draw(matrices, text, textX - 2, textY + 1, 0xFFFFFF);

        this.resetRender(matrices);
    }

    private void renderPageIndicator(MatrixStack matrices) {
        this.initRender(matrices);

        /* Shadow */
        // TODO - Shadow

        /* Background */
        float[] bgColor = this.colorManager.getFloats("widget.background");
        RenderSystem.setShaderColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        RenderSystem.setShaderTexture(0, this.background);
        drawTexture(matrices, SECTION_WIDTH, 0, 0, SECTION_HEIGHT + 1, SECTION_WIDTH, SECTION_HEIGHT, 136, 137);

        /* Re-init */
        this.resetRender(matrices);
        this.initRender(matrices, 0.75F);

        /* Text */
        String text = String.format("%d / %d", this.paginatableWidget.getCurrentPage() + 1, this.paginatableWidget.getPageCount());

        float sectionWidth = SECTION_WIDTH / SCALE_DOWN_FACTOR / 0.75F;
        float sectionHeight = SECTION_HEIGHT / SCALE_DOWN_FACTOR / 0.75F;

        float textX = sectionWidth + (sectionWidth - this.textRenderer.getWidth(text)) / 2;
        float textY = (sectionHeight - this.textRenderer.fontHeight) / 2;

        this.textRenderer.draw(matrices, text, textX, textY + 1, 0xFFFFFF);

        this.resetRender(matrices);
    }

    private void renderNextButton(MatrixStack matrices, double mouseX, double mouseY) {
        this.initRender(matrices);

        /* Shadow */
        // TODO - Shadow

        /* Background */
        float[] bgColor = this.getBackgroundColor(this.isMouseOverNextButton(mouseX, mouseY), !this.paginatableWidget.hasNextPage());
        RenderSystem.setShaderColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        RenderSystem.setShaderTexture(0, this.background);
        drawTexture(matrices, SECTION_WIDTH * 2, 0, 0, SECTION_HEIGHT * 2 + 2, SECTION_WIDTH, SECTION_HEIGHT, 136, 137);

        /* Re-init */
        this.resetRender(matrices);
        this.initRender(matrices, 0.75F);

        /* Text */
        String text = "Next";

        float sectionWidth = SECTION_WIDTH / SCALE_DOWN_FACTOR / 0.75F;
        float sectionHeight = SECTION_HEIGHT / SCALE_DOWN_FACTOR / 0.75F;

        float textX = sectionWidth * 2 + (sectionWidth - this.textRenderer.getWidth(text)) / 2;
        float textY = (sectionHeight - this.textRenderer.fontHeight) / 2;

        this.textRenderer.draw(matrices, text, textX + 2, textY + 1, 0xFFFFFF);

        this.resetRender(matrices);
    }

    private void initRender(MatrixStack matrices) {
        this.initRender(matrices, 1.0F / SCALE_DOWN_FACTOR);
    }

    private void initRender(MatrixStack matrices, float scale) {
        matrices.push();
        matrices.scale(scale, scale, 1);

        /* Init */
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    private void resetRender(MatrixStack matrices) {
        /* Reset */
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        matrices.pop();
    }

    private boolean isMouseOverPreviousButton(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY, 0, 0, SECTION_WIDTH / SCALE_DOWN_FACTOR, SECTION_HEIGHT / SCALE_DOWN_FACTOR);
    }

    private boolean isMouseOverNextButton(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY, (SECTION_WIDTH * 2) / SCALE_DOWN_FACTOR, 0, SECTION_WIDTH / SCALE_DOWN_FACTOR, SECTION_HEIGHT / SCALE_DOWN_FACTOR);
    }

    private float[] getBackgroundColor(boolean isMouseOver, boolean isDisabled) {
        if (isDisabled) {
            return this.colorManager.getFloats("widget.background.disabled");
        }

        if (isMouseOver) {
            return this.colorManager.getFloats("widget.background.hover");
        }

        return this.colorManager.getFloats("widget.background");
    }
}
