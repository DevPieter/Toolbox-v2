package nl.devpieter.toolbox.Test.Filler;

import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Test.Categories.FillerCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FillerModule5 implements IFillerModule, IToggleableModule {

    private boolean enabled = false;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NotNull List<Class<? extends ICategory>> getCategories() {
        return List.of(FillerCategory.class);
    }
}
