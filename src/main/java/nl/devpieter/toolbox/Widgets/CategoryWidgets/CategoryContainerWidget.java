package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCBorder;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.IConsumer;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class CategoryContainerWidget extends ComponentBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final float gap;
    private final IConsumer<Class<? extends ICategory>> categorySelectedCallback;

    private final SCContainer container;

    private final Animation selectedSlideAnimation;
    private SCBorder selectedBorder;

    public CategoryContainerWidget(float x, float y, float width, float height, float gap, IConsumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, height);

        this.gap = gap;
        this.categorySelectedCallback = categorySelectedCallback;

        this.container = new SCContainer(this, x, y, width, height);
        this.container.setParent(this);
        this.addChild(this.container);

        this.selectedSlideAnimation = new Animation(y, y, Duration.ofMillis(400), Easing.EXPO_OUT);
        this.selectedSlideAnimation.complete();
    }

    public void init(@NotNull List<Class<? extends ICategory>> categoryClasses) {
        this.container.clearChildren();
        if (categoryClasses.isEmpty()) return;

        int paddingX = 16;

        float x = this.getX() + paddingX;
        float y = this.getY();
        float width = this.getWidth() - paddingX * 2;

        CategoryCardWidget allCategoryCardWidget = new CategoryCardWidget(x, y, width, null, this::onCategorySelected);
        allCategoryCardWidget.setParent(this.container);
        allCategoryCardWidget.setSelected(true);

        this.container.addChild(allCategoryCardWidget);
        this.container.setFocusedChild(allCategoryCardWidget);

        y += CategoryCardWidget.HEIGHT + gap;

        for (Class<? extends ICategory> categoryClass : categoryClasses) {
            CategoryCardWidget categoryCardWidget = new CategoryCardWidget(x, y, width, categoryClass, this::onCategorySelected);
            categoryCardWidget.setParent(this.container);
            this.container.addChild(categoryCardWidget);

            y += CategoryCardWidget.HEIGHT + gap;
        }

        Color selectedColor = this.colorManager.get("category.selector");

        this.selectedBorder = new SCBorder(this.container, x, y, 1, CategoryCardWidget.HEIGHT, 2);
        this.selectedBorder.color().set(selectedColor).cornerRadius().set(8, 0, 0, 8);
        this.container.addChild(selectedBorder);
    }

    @Override
    protected void render(long context, float delta) {
        if (this.selectedBorder == null) return;
        this.selectedBorder.setY(this.selectedSlideAnimation.get());
    }

    private void onCategorySelected(@Nullable Class<? extends ICategory> categoryClass) {

        // Unselect all other category cards
        this.container.getChildren().stream()
                .filter(c -> c instanceof CategoryCardWidget cc && cc.getCategoryClass() != categoryClass)
                .forEach(c -> ((CategoryCardWidget) c).setSelected(false));

        // Find the selected category card
        IRenderable child = this.container.getChildren()
                .stream()
                .filter(c -> c instanceof CategoryCardWidget cc && cc.getCategoryClass() == categoryClass)
                .findFirst()
                .orElse(null);

        if (!(child instanceof CategoryCardWidget categoryCard)) return;

        this.container.setFocusedChild(child);
        categoryCard.setSelected(true);
        this.categorySelectedCallback.accept(categoryClass);

        this.selectedSlideAnimation.setFrom(this.selectedSlideAnimation.get());
        this.selectedSlideAnimation.setTo(child.getY());
        this.selectedSlideAnimation.reset();
    }
}
