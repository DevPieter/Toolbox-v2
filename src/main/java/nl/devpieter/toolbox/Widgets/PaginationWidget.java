package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Widgets.Abstractions.PaginatableComponentBase;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class PaginationWidget extends WidgetBase {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 45;
    public static final int GAP = 8;

    private final ColorManager colorManager = ColorManager.getInstance();

    private final PaginatableComponentBase paginatableWidget;

    private final SCCard left;
    private final SCLabel page;
    private final SCCard right;

    public PaginationWidget(float x, float y, PaginatableComponentBase paginatableWidget) {
        super(x, y, WIDTH, HEIGHT);

//        Color backgroundColor = new Color(17, 17, 17);
//        Color textColor = new Color(214, 217, 219);

        Color backgroundColor = colorManager.get("pagination.background");
        Color backgroundHoverColor = colorManager.get("pagination.background.hover");
        Color textColor = colorManager.get("pagination.text");

        STextStyle textStyle = STextStyle.of(textColor).setFontSize(18);

        float size = (width - GAP * 2) / 3;

        left = new SCCard(this, x, y, size, height);
        left.setInheritParentHover(false);
        left.color().set(backgroundColor, backgroundHoverColor);
        left.cornerRadius().set(8, 0, 0, 8);
        addChild(left);

        SText previousText = SText.of(textStyle).append("Previous");
        SCLabel previous = new SCLabel(this, x, y, size, height, previousText);
        previous.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(previous);

        SCCard middle = new SCCard(this, x + size + GAP, y, size, height);
        middle.color().set(backgroundColor);
        addChild(middle);

        page = new SCLabel(this, x + size + GAP, y, size, height, SText.of(textStyle).append("? / ?"));
        page.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(page);

        right = new SCCard(this, x + size * 2 + GAP * 2, y, size, height);
        right.setInheritParentHover(false);
        right.color().set(backgroundColor, backgroundHoverColor);
        right.cornerRadius().set(0, 8, 8, 0);
        addChild(right);

        SText nextText = SText.of(textStyle).append("Next");
        SCLabel next = new SCLabel(this, x + size * 2 + GAP * 2, y, size, height, nextText);
        next.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(next);

        this.paginatableWidget = paginatableWidget;
        paginatableWidget.subscribePageChangeListener(this::onPageChange);
    }

    @Override
    protected void render(long l, float v) {

    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!wasPressedInBounds() || !isHovered() || !isInteractable()) return;

        if (left.isHovered() && left.wasPressedInBounds()) {
            paginatableWidget.navigateToPrevious();
        } else if (right.isHovered() && right.wasPressedInBounds()) {
            paginatableWidget.navigateToNext();
        }
    }

    @Override
    public void mouseScrolled(float mouseX, float mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        if (getFocusedChild() != null) return;

        if (verticalAmount > 0 && paginatableWidget.hasPreviousPage()) {
            paginatableWidget.navigateToPrevious();
        } else if (verticalAmount < 0 && paginatableWidget.hasNextPage()) {
            paginatableWidget.navigateToNext();
        }
    }

    @Override
    public void keyReleased(int keyCode, int scanCode, int modifiers) {
        super.keyReleased(keyCode, scanCode, modifiers);
        if (getFocusedChild() != null) return;

        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT:
                paginatableWidget.navigateToPrevious();
                break;
            case GLFW.GLFW_KEY_RIGHT:
                paginatableWidget.navigateToNext();
                break;
            case GLFW.GLFW_KEY_HOME:
                paginatableWidget.navigateToFirst();
                break;
            case GLFW.GLFW_KEY_END:
                paginatableWidget.navigateToLast();
                break;
        }
    }

    private void onPageChange(int previousPage, int newPage) {

        int current = Math.max(newPage + 1, 1);
        int total = Math.max(paginatableWidget.getPageCount(), 1);

        page.setText(SText.of(this.page.textStyle()).append(current + " / " + total));
    }
}
