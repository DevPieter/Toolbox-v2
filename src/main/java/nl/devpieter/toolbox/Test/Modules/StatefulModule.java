package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IStatefulModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
