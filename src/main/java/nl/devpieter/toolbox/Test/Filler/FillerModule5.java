package nl.devpieter.toolbox.Test.Filler;

import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;

public class FillerModule5 implements IFillerModule, IToggleableModule {

    private boolean enabled = false;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
