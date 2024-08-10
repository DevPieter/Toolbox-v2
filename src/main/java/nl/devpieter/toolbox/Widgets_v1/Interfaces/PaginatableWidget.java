package nl.devpieter.toolbox.Widgets_v1.Interfaces;

public abstract class PaginatableWidget extends ParentWidget {

    public PaginatableWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract int getPageCount();

    public abstract int getCurrentPage();

    public abstract void navigateTo(int index);

    public boolean hasPage(int index) {
        if (this.getPageCount() == 0) return false;
        return index >= 0 && index < this.getPageCount();
    }

    public boolean hasNextPage() {
        return this.hasPage(this.getCurrentPage() + 1);
    }

    public boolean hasPreviousPage() {
        return this.hasPage(this.getCurrentPage() - 1);
    }

    public void navigateToNext() {
        if (!this.hasNextPage()) return;
        this.navigateTo(this.getCurrentPage() + 1);
    }

    public void navigateToPrevious() {
        if (!this.hasPreviousPage()) return;
        this.navigateTo(this.getCurrentPage() - 1);
    }

    public void navigateToFirst() {
        if (!this.hasPage(0)) return;
        this.navigateTo(0);
    }

    public void navigateToLast() {
        if (!this.hasPage(this.getPageCount() - 1)) return;
        this.navigateTo(this.getPageCount() - 1);
    }
}
