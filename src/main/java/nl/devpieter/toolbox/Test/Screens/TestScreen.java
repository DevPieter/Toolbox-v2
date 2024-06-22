package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Helpers.BackgroundHelper;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Test.Categories.DevPieterCategory;
import nl.devpieter.toolbox.Test.Categories.ExampleCategory;
import nl.devpieter.toolbox.Test.Categories.FillerCategory;
import nl.devpieter.toolbox.Widgets.CategoryWidgets.CategoryCardHolderWidget;
import nl.devpieter.toolbox.Widgets.CategoryWidgets.CategoryCardWidget;
import nl.devpieter.toolbox.Widgets.ModuleWidgets.ModuleCardPageCarouselWidget;
import nl.devpieter.toolbox.Widgets.PaginationWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;

import java.time.Duration;
import java.util.List;

public class TestScreen extends Screen {

    private final BackgroundHelper backgroundHelper = new BackgroundHelper(10, 0x80, Duration.ofMillis(250));

    private ModuleCardPageCarouselWidget carousel;

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

        addDrawableChild(carousel);
        addDrawableChild(new PaginationWidget(carousel, this.width - left25 - gap - gap, top25 - 18));

        SidebarWidget sidebar = new SidebarWidget(Text.of("Toolbox"), 0, 0, left25, this.height);
        addDrawableChild(sidebar);

        CategoryCardHolderWidget categoryCardHolderWidget = new CategoryCardHolderWidget(gap, top25 + gap, left25 - gap - gap, this.height - top25 - gap - gap, gap, value -> {
            this.carousel.init(ModuleManager.getInstance().getModules(value));
        });

        categoryCardHolderWidget.init(CategoryManager.getInstance().getCategories());
        sidebar.addChild(categoryCardHolderWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.backgroundHelper.render(matrices, this.width, this.height, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
