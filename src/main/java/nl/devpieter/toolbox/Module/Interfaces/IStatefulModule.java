package nl.devpieter.toolbox.Module.Interfaces;

import nl.devpieter.toolbox.Module.ModuleManager;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

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

    default String getFileName() {
        // TODO - Instead of using the encoded class name, use a part of the package name as the path and the class name as the file name.
        // in: nl.devpieter.toolbox.Test.Modules.TestModule
        // now: bmwuZGV2cGlldGVyLnRvb2xib3guVGVzdC5Nb2R1bGVzLlRlc3RNb2R1bGU=.json
        // new: devpieter/toolbox/TestModule_VGVzdC5Nb2R1bGVz.json (VGVzdC5Nb2R1bGVz = Test.Modules) (this is not final, just an example)

        return new String(Base64.getEncoder().encode(this.getClass().getName().getBytes())) + ".json";
    }
}
