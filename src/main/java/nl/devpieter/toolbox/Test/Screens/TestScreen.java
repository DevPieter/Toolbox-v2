package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Widgets.ModuleCardPageCarouselWidget;
import nl.devpieter.toolbox.Widgets.ModuleCardPageWidget;
import nl.devpieter.toolbox.Widgets.ModuleCardWidget;
import org.lwjgl.glfw.GLFW;

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
        this.carousel.init();

        this.addDrawableChild(this.carousel);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT && this.carousel.hasPreviousPage()) {
            this.carousel.navigateToPrevious();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT && this.carousel.hasNextPage()) {
            this.carousel.navigateToNext();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_HOME && this.carousel.hasPage(0)) {
            this.carousel.navigateToFirst();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_END && this.carousel.hasPage(this.carousel.getPageCount() - 1)) {
            this.carousel.navigateToLast();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
