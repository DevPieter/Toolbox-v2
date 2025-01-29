package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets.Abstractions.PaginatableComponentBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModuleCarouselComponent extends PaginatableComponentBase {

    private final List<ModuleContainerWidget> pageWidgets = new ArrayList<>();

    private final int gap;

    private int currentPage;

    public ModuleCarouselComponent(float x, float y, float width, float height, int gap) {
        super(x, y, width, height);

        this.gap = gap;
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        clearChildren();
        pageWidgets.clear();

        if (moduleClasses.isEmpty()) {
            this.currentPage = 0;
            this.notifyPageChange(-1, 0);

            return;
        }

        int fittingCards = ModuleContainerWidget.fittingCards(getWidth(), getHeight(), gap);
        if (fittingCards <= 0) return;

        int pages = (moduleClasses.size() + fittingCards - 1) / fittingCards;

        for (int i = 0; i < pages; i++) {
            List<Class<? extends IModule>> fitting = moduleClasses.subList(i * fittingCards, Math.min((i + 1) * fittingCards, moduleClasses.size()));

            ModuleContainerWidget page = new ModuleContainerWidget(this, getX(), getY(), getWidth(), getHeight(), gap);
            page.setVisible(i == 0);
            page.init(fitting);

            addChild(page);
            pageWidgets.add(page);
        }

        this.currentPage = 0;
        this.notifyPageChange(-1, 0);
    }

    @Override
    protected void render(long context, float delta) {

    }

    @Override
    public int getPageCount() {
        return pageWidgets.size();
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void navigateTo(int index) {
        if (index == this.currentPage) return;
        int previousPage = this.currentPage;

        pageWidgets.get(previousPage).setVisible(false);
        pageWidgets.get(index).setVisible(true);

        this.currentPage = index;
        this.notifyPageChange(previousPage, index);
    }
}
