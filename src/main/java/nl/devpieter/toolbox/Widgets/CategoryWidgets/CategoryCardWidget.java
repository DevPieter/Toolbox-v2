package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import nl.devpieter.squid.animation.ColorAnimation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.IConsumer;

import java.awt.*;
import java.time.Duration;

public class CategoryCardWidget extends ComponentBase {

    //    public static final int HEIGHT = 67;
    public static final int HEIGHT = 36 + 24;

    private final CategoryManager categoryManager = CategoryManager.getInstance();
    private final ColorManager colorManager = ColorManager.getInstance();

    private final @Nullable Class<? extends ICategory> categoryClass;
    private final IConsumer<Class<? extends ICategory>> categorySelectedCallback;

    private final ColorAnimation titleColorAnimation;
    private final SCLabel title;

    public CategoryCardWidget(float x, float y, float width, @Nullable Class<? extends ICategory> categoryClass, IConsumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, HEIGHT);

        this.categoryClass = categoryClass;
        this.categorySelectedCallback = categorySelectedCallback;

        String nameText = categoryClass == null ? "All" : this.categoryManager.getName(categoryClass).getString();
        String descriptionText = categoryClass == null ? "All categories" : this.categoryManager.getDescription(categoryClass).getString();

        Color backgroundColor = this.colorManager.get("category.card.background");
        Color backgroundHoverColor = this.colorManager.get("category.card.background.hover");
        Color titleColor = this.colorManager.get("category.card.title");
        Color titleSelectedColor = this.colorManager.get("category.card.title.selected");
        Color descriptionColor = this.colorManager.get("category.card.description");

        SCCard card = new SCCard(this, x, y, width, height);
        card.color().set(backgroundColor, backgroundHoverColor).cornerRadius().set(0, 8, 8, 0);
        this.addChild(card);

        this.titleColorAnimation = new ColorAnimation(titleColor, titleSelectedColor, Duration.ofMillis(400), Easing.EXPO_OUT);
        this.titleColorAnimation.setForward(false);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(18);
        this.title = new SCLabel(this, x + 12, y + 10, width - 32, 20, SText.of(titleStyle).append(nameText));
        this.addChild(this.title);

        STextStyle descriptionStyle = STextStyle.of(descriptionColor).setFontSize(14);
        SCLabel description = new SCLabel(this, x + 12, y + 36, width - 32, 16, SText.of(descriptionStyle).append(descriptionText));
        this.addChild(description);
    }

    @Override
    protected void render(long context, float delta) {
        this.title.text().getStyle().setColor(this.titleColorAnimation.get());
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!wasPressedInBounds() || !isHovered() || !isParentHovered() || !isInteractable()) return false;

        this.categorySelectedCallback.accept(this.categoryClass);
        return true;
    }

    public @Nullable Class<? extends ICategory> getCategoryClass() {
        return this.categoryClass;
    }

    public void setSelected(boolean selected) {
        if (this.titleColorAnimation.isForward() == selected) return;
        this.titleColorAnimation.setForward(selected);

        if (selected) this.titleColorAnimation.reset();
        else this.titleColorAnimation.complete();
    }
}
