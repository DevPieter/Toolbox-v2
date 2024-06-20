package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Widgets.Interfaces.PaginatableWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.lwjgl.glfw.GLFW;

public class PaginationWidget extends Widget {

    private final PaginatableWidget paginatableWidget;

    public PaginationWidget(PaginatableWidget paginatableWidget, int x, int y) {
        super(x, y, 160, 18);

        this.paginatableWidget = paginatableWidget;
    }

    @Override
    protected void renderWidget(MatrixStack matrices, int relativeMouseX, int relativeMouseY, float delta) {
        boolean hoveringPrevious = this.isMouseOver(relativeMouseX, relativeMouseY) && relativeMouseX < this.width / 3;
        boolean hoveringNext = this.isMouseOver(relativeMouseX, relativeMouseY) && relativeMouseX > this.width / 3 * 2;

        int cardWidth = (this.width - 5 * 2) / 3;
        this.drawPreviousButton(matrices, 0, 0, cardWidth, hoveringPrevious);
        this.drawPageNumber(matrices, cardWidth + 5, 0, cardWidth, false);
        this.drawNextButton(matrices, cardWidth * 2 + 5 * 2, 0, cardWidth, hoveringNext);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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

    private void drawPreviousButton(MatrixStack matrices, int x, int y, int width, boolean hovering) {
        this.pushMatrices(matrices, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(matrices, 0, 0, width, this.height, 6, 2, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        drawCenteredTextWithShadow(matrices, this.textRenderer, "<", width / 2, textY, 0xFFFFFF);

        this.popMatrices(matrices);
    }

    private void drawPageNumber(MatrixStack matrices, int x, int y, int width, boolean hovering) {
        this.pushMatrices(matrices, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(matrices, 0, 0, width, this.height, 2, 2, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        drawCenteredTextWithShadow(matrices, this.textRenderer, String.format("%s/%s", this.paginatableWidget.getCurrentPage() + 1, this.paginatableWidget.getPageCount()), width / 2, textY, 0xFFFFFF);

        this.popMatrices(matrices);
    }

    private void drawNextButton(MatrixStack matrices, int x, int y, int width, boolean hovering) {
        this.pushMatrices(matrices, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(matrices, 0, 0, width, this.height, 2, 6, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        drawCenteredTextWithShadow(matrices, this.textRenderer, ">", width / 2, textY, 0xFFFFFF);

        this.popMatrices(matrices);
    }

    private float[] getShadowColor(boolean hovering) {
        return this.colorManager.getFloats("widget.shadow");
    }

    private float[] getBackgroundColor(boolean hovering) {
        return this.colorManager.getFloats("widget.background");
    }
}
