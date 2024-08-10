package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Helpers.BackgroundHelper;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Widgets_v1.CategoryWidgets.CategoryCardHolderWidget;
import nl.devpieter.toolbox.Widgets_v1.ModuleWidgets.ModuleCardPageCarouselWidget;
import nl.devpieter.toolbox.Widgets_v1.ModuleWidgets.ModuleCardPageWidget;
import nl.devpieter.toolbox.Widgets_v1.PaginationWidget;
import nl.devpieter.toolbox.Widgets_v1.SidebarWidget;

import java.time.Duration;

public class TestScreen extends Screen {

    private final BackgroundHelper backgroundHelper = new BackgroundHelper(10, 0x80, Duration.ofMillis(250));

    private ModuleCardPageCarouselWidget carousel;

    private Class<? extends ICategory> categoryClass;
    private String query;

    public TestScreen() {
        super(Text.of("test"));
    }

    @Override
    protected void init() {

        int gap = 8;
        int left25 = this.width / 4;
        int top25 = this.height / 4;

        this.carousel = new ModuleCardPageCarouselWidget(left25, top25, this.width - left25, this.height - top25, gap);
        this.carousel.init(ModuleManager.getInstance().getModules());
        addDrawableChild(this.carousel);

        int fittingCardsRow = ModuleCardPageWidget.fittingCardsRow(this.carousel.getWidth(), gap);
        int lastFittingCard = ModuleCardPageWidget.xLocation(this.carousel.getWidth(), gap, fittingCardsRow - 1);
        addDrawableChild(new PaginationWidget(this.carousel, left25 + lastFittingCard, top25 - 18));

        int firstFittingCard = ModuleCardPageWidget.xLocation(this.carousel.getWidth(), gap, 0);

        int searchFieldY = fittingCardsRow != 1 ? top25 - 18 : top25 - 46;
        TextFieldWidget searchField = new TextFieldWidget(this.textRenderer, left25 + firstFittingCard, searchFieldY, 160, 18, Text.of("Search"));
        searchField.setChangedListener(value -> {
            this.query = value;
            this.search();
        });

        addDrawableChild(searchField);

        SidebarWidget sidebar = new SidebarWidget(Text.of("Toolbox"), 0, 0, left25, this.height);
        addDrawableChild(sidebar);

        CategoryCardHolderWidget categoryCardHolderWidget = new CategoryCardHolderWidget(gap, top25 + gap, left25 - gap - gap, this.height - top25 - gap - gap, gap, value -> {
            this.categoryClass = value;
            this.search();
        });

        categoryCardHolderWidget.init(CategoryManager.getInstance().getCategories(true));
        sidebar.addChild(categoryCardHolderWidget);
    }

    private void search() {
        this.carousel.init(ModuleManager.getInstance().getModules(this.categoryClass, this.query));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.backgroundHelper.render(context, this.width, this.height, delta);
        super.render(context, mouseX, mouseY, delta);

        if (!Toolbox.DEBUG) return;

        int left25 = this.width / 4;
        int gap = 8;

        int fittingCardsRow = ModuleCardPageWidget.fittingCardsRow(this.carousel.getWidth(), gap);
        int firstFittingCard = ModuleCardPageWidget.xLocation(this.carousel.getWidth(), gap, 0);
        int lastFittingCard = ModuleCardPageWidget.xLocation(this.carousel.getWidth(), gap, fittingCardsRow - 1);

        context.drawVerticalLine(left25 + firstFittingCard, this.carousel.getY(), this.carousel.getScaledHeight() + this.carousel.getY(), 0xFFFFFFFF);
        context.drawVerticalLine(left25 + lastFittingCard, this.carousel.getY(), this.carousel.getScaledHeight() + this.carousel.getY(), 0xFFFFFFFF);
    }
}
