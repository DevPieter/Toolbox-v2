package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Category.ICategory;
import nl.devpieter.toolbox.Module.Interfaces.IStatefulModule;
import nl.devpieter.toolbox.Test.Categories.DevPieterCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StatefulModule implements IStatefulModule<Boolean> {

    private boolean enabled = false;

    @Override
    public @NotNull Text getName() {
        return Text.of("Stateful Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("A module that has a state.");
    }

    @Override
    public @Nullable Boolean getState() {
        return enabled;
    }

    @Override
    public void setState(@Nullable Boolean state) {
        enabled = state != null && state;
    }

    @Override
    public @NotNull List<Class<? extends ICategory>> getCategories() {
        return List.of(DevPieterCategory.class);
    }
}
