package nl.devpieter.toolbox.Defaults.Categories;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import org.jetbrains.annotations.NotNull;

public class MiscCategory implements ICategory {

    @Override
    public @NotNull Text getName() {
        return Text.of("Misc");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("Miscellaneous modules that don't fit in any other category.");
    }

    @Override
    public int getWeight() {
        return 100;
    }
}
