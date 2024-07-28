package nl.devpieter.toolbox.Module.Interfaces;

import org.jetbrains.annotations.NotNull;

public interface IToggleableModule extends IInteractableModule {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    default @NotNull Action onEnable() {
        return Action.CONTINUE;
    }

    default void onEnabled() {
    }

    default @NotNull Action onDisable() {
        return Action.CONTINUE;
    }

    default void onDisabled() {
    }
}
