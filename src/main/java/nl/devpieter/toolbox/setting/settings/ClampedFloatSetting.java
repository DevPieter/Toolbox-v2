package nl.devpieter.toolbox.setting.settings;

import nl.devpieter.toolbox.setting.interfaces.IClampedSetting;

public class ClampedFloatSetting extends FloatSetting implements IClampedSetting<Float> {

    private float min;
    private float max;

    public ClampedFloatSetting(String identifier, Float defaultValue, float min, float max) {
        super(identifier, defaultValue);
        this.min = min;
        this.max = max;
    }

    public ClampedFloatSetting(String identifier, Float defaultValue, boolean allowNull, float min, float max) {
        super(identifier, defaultValue, allowNull);
        this.min = min;
        this.max = max;
    }

    @Override
    public Float getMin() {
        return min;
    }

    @Override
    public Float getMax() {
        return max;
    }

    @Override
    public void setMin(Float min) {
        this.min = min;
    }

    @Override
    public void setMax(Float max) {
        this.max = max;
    }
}
