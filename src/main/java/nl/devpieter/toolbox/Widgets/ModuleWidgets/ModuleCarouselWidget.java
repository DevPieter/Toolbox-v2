package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets.Abstractions.PaginatableWidgetBase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModuleCarouselWidget extends PaginatableWidgetBase {

    private final int gap;

    private final SCLabel message;
    private final SCContainer container;

    private int currentPage;

    public ModuleCarouselWidget(float x, float y, float width, float height, int gap) {
        super(x, y, width, height);
        this.gap = gap;

        STextStyle messageStyle = STextStyle.of(colorManager.get("module.carousel.message"), 20).setItalic();
        this.message = new SCLabel(this, x, y + 10, width, 24, SText.of("", messageStyle));
        this.message.alignment().set(Alignment.MIDDLE_CENTER).setVisible(false);
        this.addChild(this.message);

        this.container = new SCContainer(this, x, y, width, height);
        this.addChild(this.container);
    }

    public void init(@NotNull List<Class<? extends IModule>> moduleClasses) {
        this.message.setVisible(false);
        this.container.clearChildren();

        if (moduleClasses.isEmpty()) {
            this.currentPage = 0;
            this.notifyPageChange(-1, 0);

            this.message.text().get().set("There are no modules in this category.");
            this.message.setVisible(true);
            return;
        }

        int fittingCards = ModuleContainerWidget.fittingCards(this.getWidth(), this.getHeight(), this.gap);
        if (fittingCards <= 0) {
            this.currentPage = 0;
            this.notifyPageChange(-1, 0);

            this.message.text().get().set("Screen too small.");
            this.message.setVisible(true);
            return;
        }

        int pages = (moduleClasses.size() + fittingCards - 1) / fittingCards;

        for (int i = 0; i < pages; i++) {
            List<Class<? extends IModule>> fitting = moduleClasses.subList(i * fittingCards, Math.min((i + 1) * fittingCards, moduleClasses.size()));

            ModuleContainerWidget page = new ModuleContainerWidget(this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.gap);
            page.setParent(this.container);
            page.setVisible(i == 0);
            page.init(fitting);

            this.container.addChild(page);
        }

        this.currentPage = 0;
        this.notifyPageChange(-1, 0);
    }

    @Override
    protected void render(long context, float delta) {

    }

    @Override
    public int getPageCount() {
        return this.container.getChildren().size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void navigateTo(int index) {
        if (index == this.currentPage) return;
        int previousPage = this.currentPage;

        this.container.getChildren().get(previousPage).setVisible(false);
        this.container.getChildren().get(index).setVisible(true);

        this.currentPage = index;
        this.notifyPageChange(previousPage, index);
    }
}
