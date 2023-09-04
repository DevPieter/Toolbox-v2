package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import org.jetbrains.annotations.NotNull;

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
}
