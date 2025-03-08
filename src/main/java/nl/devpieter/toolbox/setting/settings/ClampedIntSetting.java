package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.interfaces.IClampedSetting;

public class ClampedIntSetting extends IntSetting implements IClampedSetting<Integer> {

    private int min;
    private int max;

    public ClampedIntSetting(String identifier, Integer defaultValue, int min, int max) {
        super(identifier, defaultValue);
        this.min = min;
        this.max = max;
    }

    public ClampedIntSetting(String identifier, Integer defaultValue, boolean allowNull, int min, int max) {
        super(identifier, defaultValue, allowNull);
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getMin() {
        return min;
    }

    @Override
    public Integer getMax() {
        return max;
    }

    @Override
    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public void setMax(Integer max) {
        this.max = max;
    }
}
