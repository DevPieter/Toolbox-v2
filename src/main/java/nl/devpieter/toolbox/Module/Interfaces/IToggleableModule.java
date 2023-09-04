package nl.devpieter.toolbox.Module.Interfaces;

public interface IToggleableModule extends IInteractableModule {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    default Action onEnable() {
        return Action.CONTINUE;
    }

    default void onEnabled() {
    }

    default Action onDisable() {
        return Action.CONTINUE;
    }

    default void onDisabled() {
    }
}
