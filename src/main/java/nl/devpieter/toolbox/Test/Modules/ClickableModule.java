package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Test.Categories.DevPieterCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClickableModule implements IClickableModule {

    @Override
    public @NotNull Text getName() {
        return Text.of("Clickable Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that can be clicked.");
    }

    @Override
    public void onClick(int button) {
        System.out.println("Clicked!");
    }

    @Override
    public @NotNull List<Class<? extends ICategory>> getCategories() {
        return List.of(DevPieterCategory.class);
    }
}
