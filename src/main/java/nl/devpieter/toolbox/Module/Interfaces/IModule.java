package nl.devpieter.toolbox.Module.Interfaces;

import net.minecraft.text.Text; // TODO - Replace Minecraft Text with Squid Text
import nl.devpieter.toolbox.Category.ICategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IModule {

    @NotNull
    Text getName();

    @NotNull
    Text getDescription();

    @NotNull
    default List<Class<? extends ICategory>> getCategories() {
        return List.of();
    }

    @NotNull
    default List<String> searchTerms() {
        return List.of();
    }

//    default @NotNull List<Class<? extends IModule>> getConflictingModules() {
//        return List.of();
//    }

    default void onRegistered() {
    }

    default void onUnregistered() {
    }
}
