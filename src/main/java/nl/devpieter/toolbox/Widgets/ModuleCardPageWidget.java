package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import nl.devpieter.toolbox.Callbacks.ICallback;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Widgets.Interfaces.ParentWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;

import java.util.ArrayList;
import java.util.List;

public class ModuleCardPageWidget extends ParentWidget {

    private final List<Class<? extends IModule>> moduleClasses;
    private final List<ModuleCardWidget> moduleCards = new ArrayList<>();

    private final float moduleCardSpacing;

    /* Animation */
    private long animationStartTime = 0;
    private float animationDuration = 0;
    private ICallback<Boolean> animationFinishedCallback = null;
    private float extraXStart = 0, extraXTarget = 0;
    private float extraScaleStart = 0, extraScaleTarget = 0;
    private float extraX = 0, extraScale = 0;
    private boolean isAnimating = false;


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

        float startX = (this.getWidth() - (maxCardsPerRow * (ModuleCardWidget.WIDTH + this.moduleCardSpacing / 2))) / 2;
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
        if (this.isAnimating) this.tickAnimation();
        this.moduleCards.forEach(moduleCard -> moduleCard.render(matrices, mouseX, mouseY, delta));
    }

    private void tickAnimation() {
        float time = (System.currentTimeMillis() - this.animationStartTime) / this.animationDuration;
        float blend = MathHelper.clamp(time, 0F, 1F);

        this.extraX = this.extraXStart + (this.extraXTarget - this.extraXStart) * blend;
        this.extraScale = this.extraScaleStart + (this.extraScaleTarget - this.extraScaleStart) * blend;

        if (blend < 1) return;
        this.isAnimating = false;
        if (this.animationFinishedCallback != null) this.animationFinishedCallback.invoke(true);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.moduleCards;
    }

    @Override
    public float getX() {
        return super.getX() + this.extraX;
    }

    @Override
    public float getScale() {
        return super.getScale() + this.extraScale;
    }

    public int getMaxCardsPerRow() {
        return (int) Math.floor((this.getWidth() + this.moduleCardSpacing) / (ModuleCardWidget.WIDTH + this.moduleCardSpacing));
    }

    public int getMaxCardsPerColumn() {
        return (int) Math.floor((this.getHeight() + this.moduleCardSpacing) / (ModuleCardWidget.HEIGHT + this.moduleCardSpacing));
    }

    public float getXOfModuleCard(int index) {
        if (index >= this.moduleCards.size()) return 0;
        return this.moduleCards.get(index).getX() * this.getScale();
    }

    public float getYOfModuleCard(int index) {
        if (index >= this.moduleCards.size()) return 0;
        return this.moduleCards.get(index).getY() * this.getScale();
    }

    public static int calculateFittingCards(float width, float height, float moduleCardSpacing) {
        int maxCardsPerRow = (int) Math.floor((width + moduleCardSpacing) / (ModuleCardWidget.WIDTH + moduleCardSpacing));
        int maxCardsPerColumn = (int) Math.floor((height + moduleCardSpacing) / (ModuleCardWidget.HEIGHT + moduleCardSpacing));
        return maxCardsPerRow * maxCardsPerColumn;
    }

    public void animateFromCurrent(float screenWidth, float duration, boolean toRight, ICallback<Boolean> callback) {
        this.animationDuration = duration;
        this.animationStartTime = System.currentTimeMillis();
        this.animationFinishedCallback = callback;

        this.extraXStart = this.extraX;
        this.extraScaleStart = this.extraScale;

        this.extraXTarget = toRight ? screenWidth : -screenWidth + (ModuleCardWidget.WIDTH * this.getMaxCardsPerRow());
        this.extraScaleTarget = -0.9F;

        this.isAnimating = true;
    }

    public void animateToCurrent(float screenWidth, float duration, boolean fromRight, ICallback<Boolean> callback) {
        this.animationDuration = duration;
        this.animationStartTime = System.currentTimeMillis();
        this.animationFinishedCallback = callback;

        this.extraXStart = fromRight ? screenWidth : -screenWidth + (ModuleCardWidget.WIDTH * this.getMaxCardsPerRow());
        this.extraScaleStart = -0.9F;

        this.extraXTarget = 0;
        this.extraScaleTarget = 0;

        this.isAnimating = true;
    }
}
