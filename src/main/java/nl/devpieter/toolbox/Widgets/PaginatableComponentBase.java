package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.renderable.widget.WidgetBase;
import org.apache.commons.io.function.IOBiConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PaginatableComponentBase extends WidgetBase {

    protected final List<IOBiConsumer<Integer, Integer>> pageChangeListeners = new ArrayList<>();

    public PaginatableComponentBase(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public abstract int getPageCount();

    public abstract int getCurrentPage();

    public abstract void navigateTo(int index);

    public boolean hasPage(int index) {
        if (getPageCount() == 0) return false;
        return index >= 0 && index < getPageCount();
    }

    public boolean hasNextPage() {
        return hasPage(getCurrentPage() + 1);
    }

    public boolean hasPreviousPage() {
        return hasPage(getCurrentPage() - 1);
    }

    public void navigateToNext() {
        if (!hasNextPage()) return;
        navigateTo(getCurrentPage() + 1);
    }

    public void navigateToPrevious() {
        if (!hasPreviousPage()) return;
        navigateTo(getCurrentPage() - 1);
    }

    public void navigateToFirst() {
        if (!hasPage(0)) return;
        navigateTo(0);
    }

    public void navigateToLast() {
        if (!hasPage(getPageCount() - 1)) return;
        navigateTo(getPageCount() - 1);
    }

    public void subscribePageChangeListener(IOBiConsumer<Integer, Integer> listener) {
        pageChangeListeners.add(listener);

        try {
            listener.accept(-1, getCurrentPage());
        } catch (IOException ignored) {

        }
    }

    protected void notifyPageChange(int previousPage, int newPage) {
        for (IOBiConsumer<Integer, Integer> listener : pageChangeListeners) {
            try {
                listener.accept(previousPage, newPage);
            } catch (IOException ignored) {

            }
        }
    }
}
