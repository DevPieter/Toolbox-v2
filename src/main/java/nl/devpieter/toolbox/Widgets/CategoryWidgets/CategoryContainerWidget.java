package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import nl.devpieter.squid.components.SCBorder;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.animation.Animation;
import nl.devpieter.squid.rendering.animation.Easing;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.IConsumer;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class CategoryContainerWidget extends WidgetBase {

    private final ColorManager colorManager = ColorManager.getInstance();

    private final float gap;
    private final IConsumer<Class<? extends ICategory>> categorySelectedCallback;

    private final Animation selectedSlideAnimation;
    private SCBorder selectedBorder;

    public CategoryContainerWidget(float x, float y, float width, float height, float gap, IConsumer<Class<? extends ICategory>> categorySelectedCallback) {
        super(x, y, width, height);

        this.gap = gap;
        this.categorySelectedCallback = categorySelectedCallback;

        selectedSlideAnimation = new Animation(y, y, Duration.ofMillis(400), Easing.EXPO_OUT);
        selectedSlideAnimation.complete();
    }

    public void init(@NotNull List<Class<? extends ICategory>> categoryClasses) {
        clearChildren();
        if (categoryClasses.isEmpty()) return;

        int paddingX = 16;

        float x = getX() + paddingX;
        float y = getY();
        float width = getWidth() - paddingX * 2;

        CategoryCardWidget allCategoryCardWidget = new CategoryCardWidget(x, y, width, null, this::onCategorySelected);
        allCategoryCardWidget.setSelected(true);
        addChild(allCategoryCardWidget);

        y += CategoryCardWidget.HEIGHT + gap;

        for (Class<? extends ICategory> categoryClass : categoryClasses) {
            CategoryCardWidget categoryCardWidget = new CategoryCardWidget(x, y, width, categoryClass, this::onCategorySelected);
            addChild(categoryCardWidget);
            y += CategoryCardWidget.HEIGHT + gap;
        }

//        Color selectedColor = new Color(138, 131, 235);
        Color selectedColor = colorManager.get("category.selector");

        selectedBorder = new SCBorder(this, x, y, 1, CategoryCardWidget.HEIGHT, 2);
        selectedBorder.color().set(selectedColor).cornerRadius().set(8, 0, 0, 8);
        addChild(selectedBorder);

        setFocusedChild(allCategoryCardWidget);
    }

    @Override
    protected void render(long context, float delta) {
        if (selectedBorder == null) return;
        selectedBorder.setY(selectedSlideAnimation.get());
    }

    private void onCategorySelected(@Nullable Class<? extends ICategory> categoryClass) {
        getChildren().stream()
                .filter(c -> c instanceof CategoryCardWidget cc && cc.getCategoryClass() != categoryClass)
                .forEach(c -> ((CategoryCardWidget) c).setSelected(false));

        IRenderable child = getChildren()
                .stream()
                .filter(c -> c instanceof CategoryCardWidget cc && cc.getCategoryClass() == categoryClass)
                .findFirst()
                .orElse(null);

        if (!(child instanceof CategoryCardWidget categoryCard)) return;

        setFocusedChild(child);
        categoryCard.setSelected(true);
        categorySelectedCallback.accept(categoryClass);

        selectedSlideAnimation.setFrom(selectedSlideAnimation.get());
        selectedSlideAnimation.setTo(child.getY());
        selectedSlideAnimation.reset();
    }
}
