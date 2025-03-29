package nl.devpieter.toolbox.widgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.theme.ColorManager;
import nl.devpieter.toolbox.widgets.abstractions.PaginatableWidgetBase;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class PaginationWidget extends ComponentBase {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 45;
    public static final int GAP = 8;

    private final ColorManager colorManager = ColorManager.getInstance();

    private final PaginatableWidgetBase paginatableWidget;

    private final SCCard left;
    private final SCLabel page;
    private final SCCard right;

    public PaginationWidget(float x, float y, PaginatableWidgetBase paginatableWidget) {
        super(x, y, WIDTH, HEIGHT);

        Color backgroundColor = colorManager.get("pagination.body");
        Color backgroundHoverColor = colorManager.get("pagination.body.hover");
        Color textColor = colorManager.get("pagination.body.text");

        STextStyle textStyle = STextStyle.of(textColor).setFontSize(18);

        float size = (width - GAP * 2) / 3;

        this.left = new SCCard(this, x, y, size, height);
        this.left.inheritParentHover().setFalse();
        this.left.color().set(backgroundColor, backgroundHoverColor);
        this.left.cornerRadius().set(8, 0, 0, 8);
        this.addChild(this.left);

        SText previousText = SText.of(textStyle).append("Previous");
        SCLabel previous = new SCLabel(this, x, y, size, height, previousText);
        previous.alignment().set(Alignment.MIDDLE_CENTER);
        this.addChild(previous);

        SCCard middle = new SCCard(this, x + size + GAP, y, size, height);
        middle.color().set(backgroundColor);
        this.addChild(middle);

        this.page = new SCLabel(this, x + size + GAP, y, size, height, SText.of(textStyle).append("? / ?"));
        this.page.alignment().set(Alignment.MIDDLE_CENTER);
        this.addChild(this.page);

        this.right = new SCCard(this, x + size * 2 + GAP * 2, y, size, height);
        this.right.inheritParentHover().setFalse();
        this.right.color().set(backgroundColor, backgroundHoverColor);
        this.right.cornerRadius().set(0, 8, 8, 0);
        this.addChild(this.right);

        SText nextText = SText.of(textStyle).append("Next");
        SCLabel next = new SCLabel(this, x + size * 2 + GAP * 2, y, size, height, nextText);
        next.alignment().set(Alignment.MIDDLE_CENTER);
        this.addChild(next);

        this.paginatableWidget = paginatableWidget;
        paginatableWidget.subscribePageChangeListener(this::onPageChange);
    }

    @Override
    protected void render(long l, float v) {

    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!this.wasPressedInBounds() || !this.isHovered() || !this.isInteractable()) return false;

        if (this.left.isHovered() && this.left.wasPressedInBounds()) {
            this.paginatableWidget.navigateToPrevious();
            return true;
        } else if (this.right.isHovered() && this.right.wasPressedInBounds()) {
            this.paginatableWidget.navigateToNext();
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(float mouseX, float mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        if (this.getFocusedChild() != null || !this.isParentHovered()) return false;

        if (verticalAmount > 0 && this.paginatableWidget.hasPreviousPage()) {
            this.paginatableWidget.navigateToPrevious();
            return true;
        } else if (verticalAmount < 0 && this.paginatableWidget.hasNextPage()) {
            this.paginatableWidget.navigateToNext();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        super.keyReleased(keyCode, scanCode, modifiers);
        if (this.getFocusedChild() != null) return false;

        return switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT -> {
                this.paginatableWidget.navigateToPrevious();
                yield true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                this.paginatableWidget.navigateToNext();
                yield true;
            }
            case GLFW.GLFW_KEY_HOME -> {
                this.paginatableWidget.navigateToFirst();
                yield true;
            }
            case GLFW.GLFW_KEY_END -> {
                this.paginatableWidget.navigateToLast();
                yield true;
            }
            default -> false;
        };
    }

    private void onPageChange(int previousPage, int newPage) {
        int current = Math.max(newPage + 1, 1);
        int total = Math.max(this.paginatableWidget.getPageCount(), 1);

        this.page.text().set(SText.of(this.page.text().getStyle()).append(current + " / " + total));
    }
}
