package nl.devpieter.toolbox.Defaults.States;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ToggleableState {

    private boolean enabled;

    public ToggleableState() {
        this(false);
    }

    public ToggleableState(boolean enabled) {
        this.enabled = enabled;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ToggleableState of(boolean enabled) {
        return new ToggleableState(enabled);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull ToggleableState enabled() {
        return new ToggleableState(true);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull ToggleableState disabled() {
        return new ToggleableState(false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ToggleableState setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
