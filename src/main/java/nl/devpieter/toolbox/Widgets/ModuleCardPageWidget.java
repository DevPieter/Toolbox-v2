package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets.Interfaces.ParentWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;

import java.util.ArrayList;
import java.util.List;

public class ModuleCardPageWidget extends ParentWidget {

    private final List<Class<? extends IModule>> moduleClasses;
    private final List<ModuleCardWidget> moduleCards = new ArrayList<>();

    private final float moduleCardSpacing;

    public ModuleCardPageWidget(List<Class<? extends IModule>> moduleClasses, float x, float y, float width, float height, float moduleCardSpacing) {
        super(x, y, width, height);

        this.moduleClasses = moduleClasses;
        this.moduleCardSpacing = moduleCardSpacing;

        this.init();
    }

    private void init() {
        this.moduleCards.clear();
        if (this.moduleClasses.isEmpty()) return;

        int maxCardsPerRow = this.getMaxCardsPerRow();
        if (maxCardsPerRow <= 0) return; // Screen (parent) is too small, just don't render anything

        float startX = (this.width - (maxCardsPerRow * (ModuleCardWidget.WIDTH + this.moduleCardSpacing / 2))) / 2;
        if (startX < 0) startX = 0; // Screen (parent) is too small, just start at 0

        for (int i = 0; i < this.moduleClasses.size(); i++) {
            float x = startX + (i % maxCardsPerRow) * (ModuleCardWidget.WIDTH + this.moduleCardSpacing);
            float y = (i / maxCardsPerRow) * (ModuleCardWidget.HEIGHT + this.moduleCardSpacing);

            this.moduleCards.add(new ModuleCardWidget(this.moduleClasses.get(i), x, y));
        }
    }

    @Override
    public void render(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        if (this.moduleClasses.isEmpty() || this.moduleCards.isEmpty()) return;
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta) {
        this.moduleCards.forEach(moduleCard -> moduleCard.render(matrices, mouseX, mouseY, delta));
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.moduleCards;
    }

    public int getMaxCardsPerRow() {
        return (int) Math.floor((this.width + this.moduleCardSpacing) / (ModuleCardWidget.WIDTH + this.moduleCardSpacing));
    }

    public int getMaxCardsPerColumn() {
        return (int) Math.floor((this.height + this.moduleCardSpacing) / (ModuleCardWidget.HEIGHT + this.moduleCardSpacing));
    }

    public float getXOfModuleCard(int index) {
        if (index >= this.moduleCards.size()) return 0;
        return this.moduleCards.get(index).getX() * this.scale;
    }

    public float getYOfModuleCard(int index) {
        if (index >= this.moduleCards.size()) return 0;
        return this.moduleCards.get(index).getY() * this.scale;
    }

    public static int calculateFittingCards(float width, float height, float moduleCardSpacing) {
        int maxCardsPerRow = (int) Math.floor((width + moduleCardSpacing) / (ModuleCardWidget.WIDTH + moduleCardSpacing));
        int maxCardsPerColumn = (int) Math.floor((height + moduleCardSpacing) / (ModuleCardWidget.HEIGHT + moduleCardSpacing));
        return maxCardsPerRow * maxCardsPerColumn;
    }
}
