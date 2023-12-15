package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Helpers.BackgroundHelper;
import nl.devpieter.toolbox.Widgets.ModuleCardPageCarouselWidget;
import nl.devpieter.toolbox.Widgets.PaginationWidget;

public class TestScreen extends Screen {

    private ModuleCardPageCarouselWidget carousel;

    public TestScreen() {
        super(Text.of("test"));
    }

    @Override
    protected void init() {

//        this.addDrawableChild(new ModuleCardWidget(ClickableModule.class, 50, 50));
//        this.addDrawableChild(new ModuleCardWidget(StatefulModule.class, ModuleCardWidget.WIDTH + 50 + 10, 50));
//        this.addDrawableChild(new ModuleCardWidget(TestModule.class, 50, ModuleCardWidget.HEIGHT + 50 + 10));
//        this.addDrawableChild(new ModuleCardWidget(ToggleableModule.class, ModuleCardWidget.WIDTH + 50 + 10, ModuleCardWidget.HEIGHT + 50 + 10));
//
//        float pageX = 15;
//        float pageY = this.height / 3f;
//        float pageWidth = this.width - pageX;
//        float pageHeight = this.height - pageY;
//
////        this.addDrawableChild(new ModuleCardWidget(TestModule.class, 10, 10));
//        this.addDrawableChild(new ModuleCardPageWidget(ModuleManager.getInstance().getModules(), pageX, pageY, pageWidth, pageHeight, 8));

        int moduleCardSpacing = 8;

        float x = 0;
        float y = this.height / 2.5f;
        float width = this.width - x;
        float height = this.height - y;

        this.carousel = new ModuleCardPageCarouselWidget(x, y, width, height, moduleCardSpacing, false);
        this.carousel.setPadding(8, 0, 8, 8);
//        this.carousel.setPadding(16, 16, 16, 16);
        this.carousel.init();

        this.addDrawableChild(this.carousel);

        this.addDrawableChild(new PaginationWidget(this.carousel, this.carousel.getXOfLastModuleCardInRow(), this.carousel.getYOfLastModuleCardInRow() - PaginationWidget.HEIGHT - moduleCardSpacing));
    }

    private final BackgroundHelper backgroundHelper = new BackgroundHelper(10, 0x80, 250);

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.backgroundHelper.render(matrices, this.width, this.height, delta);
        super.render(matrices, mouseX, mouseY, delta);
//        drawBorder(matrices, (int) this.carousel.getXOfModuleCard(1), (int) this.carousel.getYOfModuleCard(1), 2, 2, 0xFFFFFFFF);
    }
}
