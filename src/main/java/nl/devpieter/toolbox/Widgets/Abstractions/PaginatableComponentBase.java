package nl.devpieter.toolbox.Widgets.Abstractions;

import nl.devpieter.squid.renderable.component.ComponentBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class PaginatableComponentBase extends ComponentBase {

    protected final List<BiConsumer<Integer, Integer>> pageChangeListeners = new ArrayList<>();

    public PaginatableComponentBase(float x, float y, float width, float height) {
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

    public void subscribePageChangeListener(BiConsumer<Integer, Integer> listener) {
        this.pageChangeListeners.add(listener);

        listener.accept(-1, this.getCurrentPage());
    }

    protected void notifyPageChange(int previousPage, int newPage) {
        for (BiConsumer<Integer, Integer> listener : this.pageChangeListeners) {
            listener.accept(previousPage, newPage);
        }
    }
}
