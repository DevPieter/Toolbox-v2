package nl.devpieter.toolbox.Widgets_v1.ModuleWidgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets_v1.Interfaces.PaginatableWidget;
import nl.devpieter.toolbox.Widgets_v1.Interfaces.Widget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModuleCardPageCarouselWidget extends PaginatableWidget {

    private final List<ModuleCardPageWidget> moduleCardPageWidgets = new ArrayList<>();
    private final int gap;

    private int currentPage;

    public ModuleCardPageCarouselWidget(int x, int y, int width, int height, int gap) {
        super(x, y, width, height);
        this.gap = gap;
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        this.moduleCardPageWidgets.clear();
        this.currentPage = 0;

        if (moduleClasses.isEmpty()) return;

        int fittingCards = ModuleCardPageWidget.fittingCards(this.getWidth(), this.getHeight(), this.gap);
        if (fittingCards <= 0) return;

        int pages = (int) Math.ceil((double) moduleClasses.size() / fittingCards);

        for (int i = 0; i < pages; i++) {
            List<Class<? extends IModule>> fitting = moduleClasses.subList(i * fittingCards, Math.min((i + 1) * fittingCards, moduleClasses.size()));

            ModuleCardPageWidget page = new ModuleCardPageWidget(this.gap, this.gap, this.getWidth() - this.gap - this.gap, this.getHeight() - this.gap - this.gap, this.gap);
            page.setVisible(i == 0);
            page.init(fitting);

            this.moduleCardPageWidgets.add(page);
        }
    }

    @Override
    public int getPageCount() {
        return this.moduleCardPageWidgets.size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void navigateTo(int index) {
        if (!this.hasPage(index)) return;
        if (index == this.currentPage) return;

        this.moduleCardPageWidgets.get(this.currentPage).setVisible(false);
        this.moduleCardPageWidgets.get(index).setVisible(true);

        this.currentPage = index;
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.moduleCardPageWidgets;
    }

    @Override
    protected void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta) {
        for (ModuleCardPageWidget moduleCardPageWidget : this.moduleCardPageWidgets) {
            moduleCardPageWidget.setScale(this.getScale());
            moduleCardPageWidget.render(context, relativeMouseX, relativeMouseY, delta);
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }
}
