package nl.devpieter.toolbox.Category;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public interface ICategory {

    @NotNull
    Text getName();

    @NotNull
    Text getDescription();
}
