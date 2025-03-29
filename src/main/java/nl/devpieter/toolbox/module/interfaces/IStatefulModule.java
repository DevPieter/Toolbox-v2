package nl.devpieter.toolbox.module.interfaces;

import nl.devpieter.toolbox.module.ModuleManager;
import org.jetbrains.annotations.Nullable;

public interface IStatefulModule<T> extends IModule {

    @Nullable
    T getState();

    /***
     * It is NOT recommended to call {@link #saveState()} in this method.
     * <p>
     *     This method is called when the state is loaded from the file.
     * </p>
     * @see ModuleManager#loadState(Class)
     * @param state The state that was loaded from the file.
     */
    void setState(@Nullable T state);

    /***
     * It is NOT recommended to call {@link #saveState()} in this method.
     * <p>
     *     This method is called when the state is reset, or when something goes wrong while loading the state.
     *     It is intended to reset the state to the default one.
     * <p>
     * @see ModuleManager#loadState(Class)
     * @see ModuleManager#resetState(Class, boolean)
     */
    default void resetState() {
        setState(null);
    }

    default void saveState() {
        ModuleManager.getInstance().saveState(this.getClass());
    }
}
