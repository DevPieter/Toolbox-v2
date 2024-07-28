package nl.devpieter.toolbox.Defaults.Categories;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import org.jetbrains.annotations.NotNull;

public class ToolsCategory implements ICategory {

    @Override
    public @NotNull Text getName() {
        return Text.of("Tools");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("Tools to help you in various ways.");
    }
}
