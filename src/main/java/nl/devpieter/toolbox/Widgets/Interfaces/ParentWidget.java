package nl.devpieter.toolbox.Widgets.Interfaces;

import java.util.List;

public abstract class ParentWidget extends Widget {

    public ParentWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    private double toRelativeX(double mouseX) {
        return (mouseX - this.getX()) / this.getScale();
    }

    private double toRelativeY(double mouseY) {
        return (mouseY - this.getY()) / this.getScale();
    }

    protected abstract List<? extends Widget> getChildren();

    /* ========================= Pass-through events =========================*/
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (!this.isVisible()) return;

        for (Widget child : this.getChildren()) {
            child.mouseMoved(this.toRelativeX(mouseX), this.toRelativeY(mouseY));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.mouseClicked(this.toRelativeX(mouseX), this.toRelativeY(mouseY), button)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.mouseReleased(this.toRelativeX(mouseX), this.toRelativeY(mouseY), button)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.mouseDragged(this.toRelativeX(mouseX), this.toRelativeY(mouseY), button, deltaX, deltaY)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.mouseScrolled(this.toRelativeX(mouseX), this.toRelativeY(mouseY), horizontalAmount, verticalAmount)) return true;
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.keyPressed(keyCode, scanCode, modifiers)) return true;
        }

        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.keyReleased(keyCode, scanCode, modifiers)) return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.isVisible()) return false;

        for (Widget child : this.getChildren()) {
            if (child.charTyped(chr, modifiers)) return true;
        }

        return false;
    }
}
