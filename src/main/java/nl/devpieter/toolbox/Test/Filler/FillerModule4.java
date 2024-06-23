package nl.devpieter.toolbox.Test.Filler;

import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Test.Categories.FillerCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FillerModule4 implements IFillerModule, IClickableModule {
    @Override
    public void onClick(int button) {

    }

    @Override
    public @NotNull List<Class<? extends ICategory>> getCategories() {
        return List.of(FillerCategory.class);
    }
}
