package nl.devpieter.toolbox.Widgets_v1.ModuleWidgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.util.math.MathHelper;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets_v1.Interfaces.ParentWidget;
import nl.devpieter.toolbox.Widgets_v1.Interfaces.Widget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModuleCardPageWidget extends ParentWidget {

    private final List<ModuleCardWidget> moduleCardWidgets = new ArrayList<>();
    private final int gap;

    public ModuleCardPageWidget(int x, int y, int width, int height, int gap) {
        super(x, y, width, height);
        this.gap = gap;
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        this.moduleCardWidgets.clear();
        if (moduleClasses.isEmpty()) return;

        int maxPerRow = fittingCardsRow(this.getWidth(), this.gap);
        if (maxPerRow <= 0) return;

        int startX = (this.getWidth() - maxPerRow * (ModuleCardWidget.WIDTH + this.gap)) / 2 + this.gap / 2;

        int moduleCardWidthWithGap = ModuleCardWidget.WIDTH + this.gap;
        int moduleCardHeightWithGap = ModuleCardWidget.HEIGHT + this.gap;

        for (int i = 0; i < moduleClasses.size(); i++) {
            int x = startX + (i % maxPerRow) * moduleCardWidthWithGap;
            int y = (i / maxPerRow) * moduleCardHeightWithGap;

            this.moduleCardWidgets.add(new ModuleCardWidget(moduleClasses.get(i), x, y));
        }
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.moduleCardWidgets;
    }

    @Override
    protected void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta) {
        for (ModuleCardWidget moduleCardWidget : this.moduleCardWidgets) {
            moduleCardWidget.render(context, relativeMouseX, relativeMouseY, delta);
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    public static int fittingCards(int width, int height, int gap) {
        return fittingCardsRow(width, gap) * fittingCardsColumn(height, gap);
    }

    public static int fittingCardsRow(int width, int gap) {
        return MathHelper.floor((float) (width - gap) / (ModuleCardWidget.WIDTH + gap));
    }

    public static int fittingCardsColumn(int height, int gap) {
        return MathHelper.floor((float) (height - gap) / (ModuleCardWidget.HEIGHT + gap));
    }

    public static int xLocation(int width, int gap, int index) {
        return (width - fittingCardsRow(width, gap) * (ModuleCardWidget.WIDTH + gap)) / 2 + gap / 2 + (index % fittingCardsRow(width, gap)) * (ModuleCardWidget.WIDTH + gap);
    }
}
