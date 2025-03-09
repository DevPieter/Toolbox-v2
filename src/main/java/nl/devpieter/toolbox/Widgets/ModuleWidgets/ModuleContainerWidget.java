package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import net.minecraft.util.math.MathHelper;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets.WidgetBase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModuleContainerWidget extends WidgetBase {

    private final float gap;

    private final SCContainer container;

    public ModuleContainerWidget(float x, float y, float width, float height, float gap) {
        super(x, y, width, height);
        this.gap = gap;

        this.container = new SCContainer(this, x, y, width, height);
        this.addChild(this.container);
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        this.container.clearChildren();
        if (moduleClasses.isEmpty()) return;

        int maxPerRow = fittingCardsRow(this.getWidth(), this.gap);
        if (maxPerRow <= 0) return;

        float startX = (this.getWidth() - maxPerRow * (ModuleCardWidget.WIDTH + this.gap)) / 2 + this.gap / 2;

        float moduleCardWidthWithGap = ModuleCardWidget.WIDTH + this.gap;
        float moduleCardHeightWithGap = ModuleCardWidget.HEIGHT + this.gap;

        for (int i = 0; i < moduleClasses.size(); i++) {
            float x = startX + (i % maxPerRow) * moduleCardWidthWithGap + this.getX();
            float y = (i / maxPerRow) * moduleCardHeightWithGap + this.getY();

            this.container.addChild(new ModuleCardWidget(this, x, y, moduleClasses.get(i)));
        }
    }

    @Override
    protected void render(long context, float delta) {

    }

    public static int fittingCards(float width, float height, float gap) {
        return fittingCardsRow(width, gap) * fittingCardsColumn(height, gap);
    }

    public static int fittingCardsRow(float width, float gap) {
        return MathHelper.floor((width - gap) / (ModuleCardWidget.WIDTH + gap));
    }

    public static int fittingCardsColumn(float height, float gap) {
        return MathHelper.floor((height - gap) / (ModuleCardWidget.HEIGHT + gap));
    }
}
