package nl.devpieter.toolbox.Module.Interfaces;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public interface IModule {

    @NotNull Text getName();

    @NotNull Text getDescription();

//    default @NotNull List<Class<? extends IModule>> getConflictingModules() {
//        return List.of();
//    }

    default void onRegistered() {
    }

    default void onUnregistered() {
    }
}
