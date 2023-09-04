package nl.devpieter.toolbox.Test.Filler;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import org.jetbrains.annotations.NotNull;

public interface IFillerModule extends IModule {

    @Override
    @NotNull
    default Text getName() {
        return Text.of(this.getClass().getSimpleName());
    }

    @Override
    @NotNull
    default Text getDescription() {
        return Text.of("Lorem ipsum dolor sit amet, consec tetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip.");
    }
}
