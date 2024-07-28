package nl.devpieter.toolbox.Defaults.States;

public class ToggleableState {

    public static final ToggleableState ENABLED = new ToggleableState(true);
    public static final ToggleableState DISABLED = new ToggleableState(false);

    private boolean enabled;

    public ToggleableState() {
        this(false);
    }

    public ToggleableState(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ToggleableState setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
