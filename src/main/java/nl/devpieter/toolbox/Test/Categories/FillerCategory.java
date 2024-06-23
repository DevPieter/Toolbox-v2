package nl.devpieter.toolbox.Test.Categories;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import org.jetbrains.annotations.NotNull;

public class FillerCategory implements ICategory {

    @Override
    public @NotNull Text getName() {
        return Text.of("Filler");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("All the filler modules");
    }
}
