package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Test.Categories.DevPieterCategory;
import nl.devpieter.toolbox.Test.Categories.ExampleCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BetterToggleSprintModule implements IToggleableModule {

    private boolean enabled;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NotNull Text getName() {
        return Text.of("Better Toggle Sprint");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("Get a better toggle sprint experience.");
    }

    @Override
    public @NotNull List<Class<? extends ICategory>> getCategories() {
        return List.of(DevPieterCategory.class, ExampleCategory.class);
    }
}
