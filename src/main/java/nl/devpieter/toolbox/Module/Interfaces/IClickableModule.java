package nl.devpieter.toolbox.Module.Interfaces;

import org.jetbrains.annotations.NotNull;

public interface IClickableModule extends IInteractableModule {
    
    void onClick(int button);

    default @NotNull IInteractableModule.Action onLeftClick() {
        return Action.CONTINUE;
    }

    default @NotNull IInteractableModule.Action onMiddleClick() {
        return Action.CONTINUE;
    }

    default @NotNull IInteractableModule.Action onRightClick() {
        return Action.CONTINUE;
    }
}
