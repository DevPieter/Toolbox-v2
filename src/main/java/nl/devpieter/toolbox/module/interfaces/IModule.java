package nl.devpieter.toolbox.module.interfaces;

import nl.devpieter.toolbox.category.ICategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IModule {

    @NotNull
    String getName();

    @NotNull
    String getDescription();

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
