package nl.devpieter.toolbox.Category;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public interface ICategory {

    @NotNull
    Text getName(); // TODO - Replace Minecraft Text with String

    @NotNull
    Text getDescription(); // TODO - Replace Minecraft Text with String

    /***
     * The weight of the category. The higher the weight, the lower the category will sink in the list.
     * @return The weight of the category.
     */
    default int getWeight() {
        return 0;
    }
}
