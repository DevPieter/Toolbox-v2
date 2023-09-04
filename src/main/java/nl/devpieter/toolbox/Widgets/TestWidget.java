package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;

public class TestWidget extends Widget {

    private final int parentWidth, parentHeight;

    private long animationStart = System.currentTimeMillis();
    private float startScale = 0.5f, targetScale = 3f;
    private float duration = 5_000f;

    public TestWidget(int x, int y, int width, int height, int parentWidth, int parentHeight) {
        super(x, y, width, height);
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.scale = this.startScale;
    }

    @Override
    protected void renderWidget(MatrixStack matrices, double mouseX, double mouseY, float delta) {
//        float time = (System.currentTimeMillis() - this.animationStart) / this.duration;
//        float blend = MathHelper.clamp(time, 0, 1);
//        this.scale = this.startScale + (this.targetScale - this.startScale) * blend;
//
//        if (Math.abs(this.scale - this.targetScale) <= 0f) {
//            System.out.printf("Animation took %dms%n", System.currentTimeMillis() - this.animationStart);
//
//            float temp = this.targetScale;
//            this.targetScale = this.startScale;
//            this.startScale = temp;
//
//            this.scale = this.startScale;
//            this.animationStart = System.currentTimeMillis();
//        }

        this.scale = 1;

        float time = (System.currentTimeMillis() - this.animationStart) / this.duration;
        float blend = MathHelper.clamp(time, 0, 1);
        this.x = (int) (0 + (this.parentWidth - this.getScaleWidth()) * blend);

        if (Math.abs(this.x - (this.parentWidth - this.getScaleWidth())) <= 0f) {
            System.out.printf("Animation took %dms%n", System.currentTimeMillis() - this.animationStart);

            this.x = 0;
            this.animationStart = System.currentTimeMillis();
        }

//        this.x = (int) ((this.parentWidth / 2f) - (this.width * this.scale / 2f));
//        this.y = (int) ((this.parentHeight / 2f) - (this.height * this.scale / 2f));

        drawBorder(matrices, 0, 0, (int) this.width, (int) this.height, 0xFFf57542);

        drawTextWithShadow(matrices, this.textRenderer, "1", -this.textRenderer.getWidth("1"), 0, 0x5742f5);
        drawTextWithShadow(matrices, this.textRenderer, "2", (int) width, 0, 0x5742f5);
        drawTextWithShadow(matrices, this.textRenderer, "3", -this.textRenderer.getWidth("3"), (int) (height - this.textRenderer.fontHeight), 0x5742f5);
        drawTextWithShadow(matrices, this.textRenderer, "4", (int) width, (int) (height - this.textRenderer.fontHeight), 0x5742f5);

        int textLength = this.textRenderer.getWidth("tester");
        int textHeight = this.textRenderer.fontHeight;

        mouseX = MathHelper.clamp(mouseX, 0, this.width - textLength);
        mouseY = MathHelper.clamp(mouseY, 0, this.height - textHeight);

        drawTextWithShadow(matrices, this.textRenderer, "tester", (int) mouseX, (int) mouseY, 0xf5426f);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
