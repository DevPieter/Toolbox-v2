package nl.devpieter.toolbox.Module.Interfaces;

import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public interface IStatefulModule<T> extends IModule {

    @Nullable T getState();

    void setState(@Nullable T state);

    default void resetState() {
        setState(null);
    }

    default String getFileName() {
        return new String(Base64.getEncoder().encode(this.getClass().getName().getBytes())) + ".json";
    }
}
