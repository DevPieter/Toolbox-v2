package nl.devpieter.toolbox.Widgets_old;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Widgets_old.Interfaces.PaginatableWidget;
import nl.devpieter.toolbox.Widgets_old.Interfaces.Widget;

import java.util.ArrayList;
import java.util.List;

public class ModuleCardPageCarouselWidget extends PaginatableWidget {

    private final ModuleManager moduleManager = ModuleManager.getInstance();
    private final List<ModuleCardPageWidget> moduleCardPages = new ArrayList<>();

    private final float moduleCardSpacing;
    private float paddingLeft, paddingTop, paddingRight, paddingBottom;

    private int currentPageIndex = 0;

    public ModuleCardPageCarouselWidget(float x, float y, float width, float height, float moduleCardSpacing, boolean init) {
        super(x, y, width, height);

        this.moduleCardSpacing = moduleCardSpacing;
        if (init) this.init();
    }

    public void setPadding(float left, float top, float right, float bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
    }

    public void init() {
        this.moduleCardPages.clear();
        if (this.moduleManager.getModules().isEmpty()) return;

        float x = this.paddingLeft;
        float y = this.paddingTop;
        float width = this.getWidth() - this.paddingLeft - this.paddingRight;
        float height = this.getHeight() - this.paddingTop - this.paddingBottom;

        int moduleCardsPerPage = ModuleCardPageWidget.calculateFittingCards(width, height, this.moduleCardSpacing);
        if (moduleCardsPerPage <= 0) return;

        List<Class<? extends IModule>> moduleClasses = this.moduleManager.getModules();

        int moduleCount = moduleClasses.size();
        int pageCount = (int) Math.ceil((double) moduleCount / moduleCardsPerPage);

        // Update the current page index if it's out of bounds
        if (this.currentPageIndex >= pageCount) this.currentPageIndex = pageCount - 1;

        for (int i = 0; i < pageCount; i++) {
            int startIndex = i * moduleCardsPerPage;
            int endIndex = Math.min(startIndex + moduleCardsPerPage, moduleCount);

            ModuleCardPageWidget page = new ModuleCardPageWidget(moduleClasses.subList(startIndex, endIndex), x, y, width, height, this.moduleCardSpacing);
            page.setVisible(i == this.currentPageIndex);

            this.moduleCardPages.add(page);
        }
    }

    @Override
    public void render(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        if (this.moduleCardPages.isEmpty()) return;
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        this.moduleCardPages.forEach(moduleCardPageWidget -> moduleCardPageWidget.render(matrices, mouseX, mouseY, delta));
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.moduleCardPages;
    }

    @Override
    public int getPageCount() {
        return this.moduleCardPages.size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPageIndex;
    }

    @Override
    public void navigateTo(int index) {
        if (!this.hasPage(index)) return;
        if (index == this.currentPageIndex) return;

        long duration = this.currentPageIndex - index == 1 || this.currentPageIndex - index == -1 ? 225 : 175;

        ModuleCardPageWidget currentPage = this.moduleCardPages.get(this.currentPageIndex);
        currentPage.animateFromCurrent(this.getWidth(), duration, index < this.currentPageIndex, (hasFinished) -> currentPage.setVisible(false));

        ModuleCardPageWidget newPage = this.moduleCardPages.get(index);
        newPage.animateToCurrent(this.getWidth(), duration, index > this.currentPageIndex, null);
        newPage.setVisible(true);

        this.currentPageIndex = index;
    }

    public float getXOfModuleCard(int index) {
        ModuleCardPageWidget currentPage = this.moduleCardPages.get(this.currentPageIndex);
        return ((this.getX() + this.paddingLeft) + (currentPage.getXOfModuleCard(index) * this.getScale()));
    }

    public float getYOfModuleCard(int index) {
        ModuleCardPageWidget currentPage = this.moduleCardPages.get(this.currentPageIndex);
        return ((this.getY() + this.paddingTop) + (currentPage.getYOfModuleCard(index) * this.getScale()));
    }

    public float getXOfLastModuleCardInRow() {
        return this.getXOfModuleCard(this.moduleCardPages.get(this.currentPageIndex).getMaxCardsPerRow() - 1);
    }

    public float getYOfLastModuleCardInRow() {
        return this.getYOfModuleCard(this.moduleCardPages.get(this.currentPageIndex).getMaxCardsPerRow() - 1);
    }
}
