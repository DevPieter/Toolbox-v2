package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Helpers.BackgroundHelper;
import nl.devpieter.toolbox.Test.Filler.*;
import nl.devpieter.toolbox.Test.Modules.ClickableModule;
import nl.devpieter.toolbox.Test.Modules.TestModule;
import nl.devpieter.toolbox.Test.Modules.ToggleableModule;
import nl.devpieter.toolbox.Widgets.ModuleCardWidget;

import java.time.Duration;

public class TestScreen extends Screen {

    private final BackgroundHelper backgroundHelper = new BackgroundHelper(10, 0x80, Duration.ofMillis(250));


    public TestScreen() {
        super(Text.of("test"));
    }

    @Override
    protected void init() {

        addDrawableChild(new ModuleCardWidget(TestModule.class, 75, 100));
        addDrawableChild(new ModuleCardWidget(ToggleableModule.class, 75 + 160 + 10, 100));
        addDrawableChild(new ModuleCardWidget(ClickableModule.class, 75 + 160 + 10 + 160 + 10, 100));

        addDrawableChild(new ModuleCardWidget(FillerModule1.class, 75, 100 + 50 + 10));
        addDrawableChild(new ModuleCardWidget(FillerModule2.class, 75 + 160 + 10, 100 + 50 + 10));
        addDrawableChild(new ModuleCardWidget(FillerModule3.class, 75 + 160 + 10 + 160 + 10, 100 + 50 + 10));

        addDrawableChild(new ModuleCardWidget(FillerModule4.class, 75, 100 + 50 + 10 + 50 + 10));
        addDrawableChild(new ModuleCardWidget(FillerModule5.class, 75 + 160 + 10, 100 + 50 + 10 + 50 + 10));
        addDrawableChild(new ModuleCardWidget(FillerModule6.class, 75 + 160 + 10 + 160 + 10, 100 + 50 + 10 + 50 + 10));

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.backgroundHelper.render(matrices, this.width, this.height, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
