package nl.devpieter.toolbox.widgets.categoryWidgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCBorder;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.toolbox.category.ICategory;
import nl.devpieter.toolbox.widgets.WidgetBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class CategoryContainerWidget extends WidgetBase {

    private final float gap;
    private final Consumer<Class<? extends ICategory>> categorySelectedCallback;

    private final SCContainer container;

    private final Animation selectedSlideAnimation;
    private SCBorder selectedBorder;

    public CategoryContainerWidget(float x, float y, float width, float height, float gap, Consumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, height);

        this.gap = gap;
        this.categorySelectedCallback = categorySelectedCallback;

        this.container = new SCContainer(this, x, y, width, height);
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

        CategoryCardWidget allCategoryCardWidget = new CategoryCardWidget(this.container, x, y, width, null, this::onCategorySelected);
        allCategoryCardWidget.setSelected(true);

        this.container.addChild(allCategoryCardWidget);
        this.container.setFocusedChild(allCategoryCardWidget);

        y += CategoryCardWidget.HEIGHT + gap;

        for (Class<? extends ICategory> categoryClass : categoryClasses) {
            CategoryCardWidget categoryCardWidget = new CategoryCardWidget(this.container, x, y, width, categoryClass, this::onCategorySelected);
            this.container.addChild(categoryCardWidget);

            y += CategoryCardWidget.HEIGHT + gap;
        }

        this.selectedBorder = new SCBorder(this.container, x, y, 1, CategoryCardWidget.HEIGHT - 2, 2);
        this.selectedBorder.color().set(colorManager.get("category.selector")).cornerRadius().set(8, 0, 0, 8);
        this.container.addChild(selectedBorder);
    }

    @Override
    protected void render(long context, float delta) {
        if (this.selectedBorder == null) return;
        this.selectedBorder.setY(this.selectedSlideAnimation.get() + 1);
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
