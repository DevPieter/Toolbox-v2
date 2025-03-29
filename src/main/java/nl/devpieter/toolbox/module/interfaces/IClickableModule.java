package nl.devpieter.toolbox.module.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IClickableModule extends IInteractableModule {

    default void onClick(int button) {
    }

    default @NotNull Action onLeftClick() {
        return Action.CONTINUE;
    }

    default @NotNull Action onMiddleClick() {
        return Action.CONTINUE;
    }

    default @NotNull Action onRightClick() {
        return Action.CONTINUE;
    }
}
