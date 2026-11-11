package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.network.chat.Component;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.toolbox.category.CategoryManager;
import nl.devpieter.toolbox.module.ModuleManager;
import nl.devpieter.toolbox.widgets.categoryWidgets.CategoryContainerWidget;
import nl.devpieter.toolbox.widgets.ContentWidget;
import nl.devpieter.toolbox.widgets.moduleWidgets.ModuleCarouselWidget;
import nl.devpieter.toolbox.widgets.PaginationWidget;
import nl.devpieter.toolbox.widgets.SidebarWidget;

public class ModulesScreen extends SquidScreen {

    public ModulesScreen() {
        super(Component.literal("test"));
    }

    @Override
    protected void init() {
        super.init();
        int gap = 16;

        ContentWidget content = new ContentWidget(424 + 16, 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 32);

        ModuleCarouselWidget moduleCarouselWidget = new ModuleCarouselWidget(424 + 16, 324 + 8 + 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 324 - 8 - 16 - 16, 32);
        moduleCarouselWidget.init(ModuleManager.getInstance().getModules());
        moduleCarouselWidget.setParent(content);
        content.addChild(moduleCarouselWidget);

        float centerPaginationX = 424 + 16 + (this.windowWidth - 424 - 16 - 16) / 2F - PaginationWidget.WIDTH / 2F;

        PaginationWidget paginationWidget = new PaginationWidget(centerPaginationX, 324 - PaginationWidget.HEIGHT - 16, moduleCarouselWidget);
        paginationWidget.setParent(content);
        content.addChild(paginationWidget);

        this.addRenderable(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);
        CategoryContainerWidget categoryContainer = new CategoryContainerWidget(0, 324 + 8 + 16, 424, sidebar.getHeight() - 324 - 15 - 32, gap, aClass -> {
            moduleCarouselWidget.init(ModuleManager.getInstance().getModules(aClass));
        });

        categoryContainer.init(CategoryManager.getInstance().getCategories());
        categoryContainer.setParent(sidebar);
        sidebar.addChild(categoryContainer);

        this.addRenderable(sidebar);
    }

}
