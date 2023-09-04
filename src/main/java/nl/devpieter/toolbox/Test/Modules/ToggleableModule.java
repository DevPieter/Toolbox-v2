package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import org.jetbrains.annotations.NotNull;

public class ToggleableModule implements IToggleableModule {

    private boolean enabled = false;

    @Override
    public @NotNull Text getName() {
        return Text.of("Toggleable Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("This is a toggleable module.");
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
