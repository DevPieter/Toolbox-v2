package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.animation.Animation;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ModuleCarouselWidget extends WidgetBase {

    private final List<ModuleContainerWidget> pageWidgets = new ArrayList<>();

    private final int gap;

    private int currentPage;

    // TODO - Remove debug
    private Animation autoSwapAnimation;

    public ModuleCarouselWidget(float x, float y, float width, float height, int gap) {
        super(x, y, width, height);

        this.gap = gap;
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        clearChildren();
        currentPage = 0;

        if (moduleClasses.isEmpty()) return;

        int fittingCards = ModuleContainerWidget.fittingCards(getWidth(), getHeight(), gap);
        if (fittingCards <= 0) return;

        int pages = (moduleClasses.size() + fittingCards - 1) / fittingCards;

        for (int i = 0; i < pages; i++) {
            List<Class<? extends IModule>> fitting = moduleClasses.subList(i * fittingCards, Math.min((i + 1) * fittingCards, moduleClasses.size()));

//            ModuleContainerWidget page = new ModuleContainerWidget(gap, gap, getWidth() - gap - gap, getHeight() - gap - gap, gap);
            ModuleContainerWidget page = new ModuleContainerWidget(getX(), getY(), getWidth(), getHeight(), gap);
            page.setVisible(i == 0);
            page.init(fitting);

            addChild(page);
            pageWidgets.add(page);
        }

        // TODO - Remove debug
        autoSwapAnimation = new Animation(0, pages - 1, Duration.ofSeconds(5), true);
    }

    @Override
    protected void render(long context, float delta) {
        if (autoSwapAnimation == null) return;

        // TODO - Remove debug
        if (autoSwapAnimation.isDone()) {
            autoSwapAnimation.setForward(!autoSwapAnimation.isForward());
            autoSwapAnimation.reset();
        }

        int index = Math.round(autoSwapAnimation.get());
        if (index != currentPage) navigateTo(index);
    }

    public void navigateTo(int index) {
        if (index == this.currentPage) return;

        pageWidgets.get(this.currentPage).setVisible(false);
        pageWidgets.get(index).setVisible(true);

        this.currentPage = index;
    }
}
