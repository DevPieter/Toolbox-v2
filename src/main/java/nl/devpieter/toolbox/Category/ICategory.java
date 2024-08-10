package nl.devpieter.toolbox.Category;

import net.minecraft.text.Text; // TODO - Replace Minecraft Text with Squid Text
import org.jetbrains.annotations.NotNull;

public interface ICategory {

    @NotNull
    Text getName();

    @NotNull
    Text getDescription();

    /***
     * The weight of the category. The higher the weight, the lower the category will sink in the list.
     * @return The weight of the category.
     */
    default int getWeight() {
        return 0;
    }
}
