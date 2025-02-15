package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import net.minecraft.util.math.MathHelper;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModuleContainerWidget extends ComponentBase {

    private final float gap;

    public ModuleContainerWidget(IRenderable parent, float x, float y, float width, float height, float gap) {
        super(x, y, width, height);
        this.setParent(parent);

        this.gap = gap;
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        clearChildren();
        if (moduleClasses.isEmpty()) return;

        int maxPerRow = fittingCardsRow(getWidth(), gap);
        if (maxPerRow <= 0) return;

        float startX = (getWidth() - maxPerRow * (ModuleCardWidget.WIDTH + gap)) / 2 + gap / 2;

        float moduleCardWidthWithGap = ModuleCardWidget.WIDTH + this.gap;
        float moduleCardHeightWithGap = ModuleCardWidget.HEIGHT + this.gap;

        for (int i = 0; i < moduleClasses.size(); i++) {
            float x = startX + (i % maxPerRow) * moduleCardWidthWithGap + getX();
            float y = (i / maxPerRow) * moduleCardHeightWithGap + getY();

            addChild(new ModuleCardWidget(this, x, y, moduleClasses.get(i)));
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
