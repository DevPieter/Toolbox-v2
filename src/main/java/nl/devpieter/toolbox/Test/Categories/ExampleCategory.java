package nl.devpieter.toolbox.Test.Categories;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import org.jetbrains.annotations.NotNull;

public class ExampleCategory implements ICategory {

    @Override
    public @NotNull Text getName() {
        return Text.of("Example");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("Example category");
    }
}
