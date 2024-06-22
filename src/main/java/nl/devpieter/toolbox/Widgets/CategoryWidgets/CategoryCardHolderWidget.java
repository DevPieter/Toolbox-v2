package nl.devpieter.toolbox.Widgets.CategoryWidgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Callbacks.ICallback;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Widgets.Interfaces.ParentWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryCardHolderWidget extends ParentWidget {

    private final List<CategoryCardWidget> categoryCardWidgets = new ArrayList<>();

    private final int gap;
    private final ICallback<@Nullable Class<? extends ICategory>> callback;

    public CategoryCardHolderWidget(int x, int y, int width, int height, int gap, ICallback<@Nullable Class<? extends ICategory>> callback) {
        super(x, y, width, height);

        this.gap = gap;
        this.callback = callback;
    }

    public void init(@NotNull List<Class<? extends ICategory>> categoryClasses) {
        this.categoryCardWidgets.clear();
        if (categoryClasses.isEmpty()) return;

        CategoryCardWidget allCategoryCardWidget = new CategoryCardWidget(null, 0, 0, this.getWidth(), this::onCategorySelected);
        allCategoryCardWidget.setSelected(true);
        this.categoryCardWidgets.add(allCategoryCardWidget);

        int y = CategoryCardWidget.HEIGHT + this.gap;

        for (Class<? extends ICategory> categoryClass : categoryClasses) {
            CategoryCardWidget categoryCardWidget = new CategoryCardWidget(categoryClass, 0, y, this.getWidth(), this::onCategorySelected);
            this.categoryCardWidgets.add(categoryCardWidget);
            y += CategoryCardWidget.HEIGHT + this.gap;
        }
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.categoryCardWidgets;
    }

    @Override
    protected void renderWidget(MatrixStack matrices, int relativeMouseX, int relativeMouseY, float delta) {

        // TODO - Add scrolling

        for (Widget child : this.categoryCardWidgets) {
            child.render(matrices, relativeMouseX, relativeMouseY, delta);
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }
    
    private void onCategorySelected(@Nullable Class<? extends ICategory> categoryClass) {
        for (CategoryCardWidget categoryCardWidget : this.categoryCardWidgets) {
            categoryCardWidget.setSelected(categoryCardWidget.getCategoryClass() == categoryClass);
        }

        this.callback.invoke(categoryClass);
    }
}
