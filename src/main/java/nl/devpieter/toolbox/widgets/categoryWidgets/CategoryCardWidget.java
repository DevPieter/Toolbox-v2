package nl.devpieter.toolbox.widgets.categoryWidgets;

import nl.devpieter.squid.animation.ColorAnimation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.category.ICategory;
import nl.devpieter.toolbox.widgets.WidgetBase;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.IConsumer;

import java.time.Duration;

public class CategoryCardWidget extends WidgetBase {

    public static final int HEIGHT = 36 + 24;

    private final @Nullable Class<? extends ICategory> categoryClass;
    private final IConsumer<Class<? extends ICategory>> categorySelectedCallback;

    private final ColorAnimation titleColorAnimation;
    private final SCLabel title;

    public CategoryCardWidget(IRenderable parent, float x, float y, float width, @Nullable Class<? extends ICategory> categoryClass, IConsumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, HEIGHT);
        this.setParent(parent);

        this.categoryClass = categoryClass;
        this.categorySelectedCallback = categorySelectedCallback;

        String nameText = categoryClass == null ? "All" : categoryManager.getName(categoryClass);
        String descriptionText = categoryClass == null ? "All categories" : categoryManager.getDescription(categoryClass);

        this.titleColorAnimation = new ColorAnimation(colorManager.get("category.card.body.title"), colorManager.get("category.card.body.title.selected"), Duration.ofMillis(400), Easing.EXPO_OUT);
        this.titleColorAnimation.setForward(false);

        SCCard card = new SCCard(this, x, y, width, height);
        card.color().set(colorManager.get("category.card.body"), colorManager.get("category.card.body.hover")).cornerRadius().set(0, 8, 8, 0);
        this.addChild(card);

        STextStyle titleStyle = STextStyle.of().setBold().setFontSize(18);
        this.title = new SCLabel(card, x + 12, y + 10, width - 32, 20, SText.of(titleStyle).append(nameText));
        card.addChild(this.title);

        STextStyle descriptionStyle = STextStyle.of(colorManager.get("category.card.body.description"), 14);
        SCLabel description = new SCLabel(card, x + 12, y + 36, width - 32, 16, SText.of(descriptionStyle).append(descriptionText));
        card.addChild(description);
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
