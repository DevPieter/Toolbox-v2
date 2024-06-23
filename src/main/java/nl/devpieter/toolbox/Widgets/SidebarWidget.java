package nl.devpieter.toolbox.Widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import nl.devpieter.toolbox.Helpers.AnimationHelper;
import nl.devpieter.toolbox.Utils.TextUtils;
import nl.devpieter.toolbox.Widgets.Interfaces.ParentWidget;
import nl.devpieter.toolbox.Widgets.Interfaces.Widget;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SidebarWidget extends ParentWidget {

    private final List<Widget> children = new ArrayList<>();
    private final Text title;

    /* Animation */
    private final AnimationHelper offsetXAnimation;

    public SidebarWidget(Text title, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.title = title;

        this.offsetXAnimation = new AnimationHelper(-width, 0, Duration.ofMillis(400), AnimationHelper.Easing.EXPO_IN_OUT);
    }

    public void addChild(Widget widget) {
        this.children.add(widget);
    }

    public void removeChild(Widget widget) {
        this.children.remove(widget);
    }

    @Override
    protected List<? extends Widget> getChildren() {
        return this.children;
    }

    @Override
    protected void renderWidget(DrawContext context, int relativeMouseX, int relativeMouseY, float delta) {
        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.translate(this.offsetXAnimation.get(), 0, 0);

        Color color = new Color(15, 15, 15);
        context.fill(0, 0, this.getScaledWidth(), this.getScaledHeight(), color.getRGB());

        TextUtils.drawCenteredText(context, this.textRenderer, this.title, 0, 20, this.getScaledWidth(), 2.0f, true);

        for (Widget child : this.children) {
            child.setScale(this.getScale());
            child.render(context, relativeMouseX, relativeMouseY, delta);
        }

        matrices.pop();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        // TODO - Narrations
    }
}
