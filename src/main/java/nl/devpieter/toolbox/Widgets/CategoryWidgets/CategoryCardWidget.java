package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import nl.devpieter.squid.animation.ColorAnimation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.IConsumer;

import java.awt.*;
import java.time.Duration;

public class CategoryCardWidget extends WidgetBase {

    //    public static final int HEIGHT = 67;
    public static final int HEIGHT = 36 + 24;

    private final ColorManager colorManager = ColorManager.getInstance();

    private final @Nullable Class<? extends ICategory> categoryClass;
    private final IConsumer<Class<? extends ICategory>> categorySelectedCallback;

    private final ColorAnimation titleColorAnimation;
    private final SCLabel title;

    public CategoryCardWidget(float x, float y, float width, @Nullable Class<? extends ICategory> categoryClass, IConsumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, HEIGHT);

        this.categoryClass = categoryClass;
        this.categorySelectedCallback = categorySelectedCallback;

        CategoryManager categoryManager = CategoryManager.getInstance();
        String nameText = categoryClass == null ? "All" : categoryManager.getName(categoryClass).getString();
        String descriptionText = categoryClass == null ? "All categories" : categoryManager.getDescription(categoryClass).getString();

        Color backgroundColor = colorManager.get("category.card.background");
        Color backgroundHoverColor = colorManager.get("category.card.background.hover");
        Color titleColor = colorManager.get("category.card.title");
        Color titleSelectedColor = colorManager.get("category.card.title.selected");
        Color descriptionColor = colorManager.get("category.card.description");

        SCCard card = new SCCard(this, x, y, width, height);
        card.color().set(backgroundColor, backgroundHoverColor).cornerRadius().set(0, 8, 8, 0);
        addChild(card);

        titleColorAnimation = new ColorAnimation(titleColor, titleSelectedColor, Duration.ofMillis(400), Easing.EXPO_OUT);
        titleColorAnimation.setForward(false);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(18);
        title = new SCLabel(this, x + 12, y + 10, width - 32, 20, SText.of(titleStyle).append(nameText));
        addChild(title);

        STextStyle descriptionStyle = STextStyle.of(descriptionColor).setFontSize(14);
        SCLabel description = new SCLabel(this, x + 12, y + 36, width - 32, 16, SText.of(descriptionStyle).append(descriptionText));
        addChild(description);
    }

    @Override
    protected void render(long context, float delta) {
        title.textStyle().setColor(titleColorAnimation.get());
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!wasPressedInBounds() || !isHovered() || !isInteractable()) return;

        categorySelectedCallback.accept(categoryClass);
    }

    public @Nullable Class<? extends ICategory> getCategoryClass() {
        return categoryClass;
    }

    public void setSelected(boolean selected) {
        if (titleColorAnimation.isForward() == selected) return;
        titleColorAnimation.setForward(selected);

        if (selected) titleColorAnimation.reset();
        else titleColorAnimation.complete();
    }
}
