package nl.devpieter.toolbox.Test.Filler;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import org.jetbrains.annotations.NotNull;

public interface IFillerModule extends IModule {

    @Override
    @NotNull
    default Text getName() {
        return Text.of(this.getClass().getSimpleName());
//        return Text.of(this.getClass().getSimpleName() + " with some 1 2,, 3 4 5 6 7 8 9");
    }

    @Override
    @NotNull
    default Text getDescription() {
        return Text.of("Lorem ipsum dolor sit amet, consec tetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
//        return Text.of("Lorem ipsum dolor sit amet, consec tetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip. Lorem ipsum dolor sit amet, consec tetu test ing is the best way to test  bla dsfjlsdjf ekjlsf jtesitusiot testing is 1 2 3 4 5 6 7 8");
    }
}
