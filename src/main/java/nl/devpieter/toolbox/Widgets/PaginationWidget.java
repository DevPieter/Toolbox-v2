package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
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
    protected void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta) {
        boolean hoveringPrevious = this.isMouseOver(relativeMouseX, relativeMouseY) && relativeMouseX < this.width / 3;
        boolean hoveringNext = this.isMouseOver(relativeMouseX, relativeMouseY) && relativeMouseX > this.width / 3 * 2;

        int cardWidth = (this.width - 5 * 2) / 3;
        this.drawPreviousButton(context, 0, 0, cardWidth, hoveringPrevious);
        this.drawPageNumber(context, cardWidth + 5, 0, cardWidth);
        this.drawNextButton(context, cardWidth * 2 + 5 * 2, 0, cardWidth, hoveringNext);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0 && this.paginatableWidget.hasPreviousPage()) {
            this.paginatableWidget.navigateToPrevious();
            return true;
        }

        if (verticalAmount < 0 && this.paginatableWidget.hasNextPage()) {
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

    private void drawPreviousButton(DrawContext context, int x, int y, int width, boolean hovering) {
        this.pushMatrices(context, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(context, 0, 0, width, this.height, 0, 0, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        context.drawCenteredTextWithShadow(this.textRenderer, "<", width / 2, textY, 0xFFFFFF);

        this.popMatrices(context);
    }

    private void drawPageNumber(DrawContext context, int x, int y, int width) {
        this.pushMatrices(context, x, y);

        float[] shadowColor = this.getShadowColor(false);
        float[] backgroundColor = this.getBackgroundColor(false);

        this.cardManager.drawCardWithShadow(context, 0, 0, width, this.height, 0, 0, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        context.drawCenteredTextWithShadow(this.textRenderer, String.format("%s/%s", this.paginatableWidget.getCurrentPage() + 1, this.paginatableWidget.getPageCount()), width / 2, textY, 0xFFFFFF);

        this.popMatrices(context);
    }

    private void drawNextButton(DrawContext context, int x, int y, int width, boolean hovering) {
        this.pushMatrices(context, x, y);

        float[] shadowColor = this.getShadowColor(hovering);
        float[] backgroundColor = this.getBackgroundColor(hovering);

        this.cardManager.drawCardWithShadow(context, 0, 0, width, this.height, 0, 0, 1, 4, backgroundColor, shadowColor);

        int textY = this.getScaledHeight() / 2 - this.textRenderer.fontHeight / 2 + 1;
        context.drawCenteredTextWithShadow(this.textRenderer, ">", width / 2, textY, 0xFFFFFF);

        this.popMatrices(context);
    }

    private float[] getShadowColor(boolean hovering) {
        return this.colorManager.getFloats("widget.shadow");
    }

    private float[] getBackgroundColor(boolean hovering) {
        return this.colorManager.getFloats("widget.background");
    }
}
