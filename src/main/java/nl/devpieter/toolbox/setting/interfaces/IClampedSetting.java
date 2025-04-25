package nl.devpieter.toolbox.setting.interfaces;

public interface IClampedSetting<T extends Number> extends ISetting<T> {

    T getMin();

    T getMax();

    void setMin(T min);

    void setMax(T max);
}
