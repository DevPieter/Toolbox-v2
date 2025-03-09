package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Widgets.CategoryWidgets.CategoryContainerWidget;
import nl.devpieter.toolbox.Widgets.ContentWidget;
import nl.devpieter.toolbox.Widgets.ModuleWidgets.ModuleCarouselWidget;
import nl.devpieter.toolbox.Widgets.PaginationWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;

public class ModulesScreen extends SquidScreen {

    public ModulesScreen() {
        super(Text.of("test"));
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

    @Override
    protected void render(long context, float mouseX, float mouseY, float delta) {
//        if (!Toolbox.KAUSHAN_SCRIPT.getRegular().loaded()) { // TODO - Change this to a more reliable method
//            FontUtils.loadFont(context, Toolbox.KAUSHAN_SCRIPT);
//        }

//        Color top = new Color(9, 9, 9, 103);
//        NVGHelper.drawRoundedRec(context, 424 + 16, 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 32, 16, top.getRGB());

        super.render(context, mouseX, mouseY, delta);
    }
}
