package nl.devpieter.toolbox.setting.widgetSettings;

import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.SettingWidgets.NumberSettingWidget;
import nl.devpieter.toolbox.setting.abstractions.WidgetSettingBase;

import java.lang.reflect.Type;

public class NumberWidgetSetting<T extends Number> extends WidgetSettingBase<T> {

    private final Type type;

    private T min;
    private T max;

    public NumberWidgetSetting(String identifier, T defaultValue, String displayName, String description, Type type, T min, T max) {
        super(identifier, defaultValue, displayName, description);
        this.type = type;
        this.min = min;
        this.max = max;
    }

    public NumberWidgetSetting(String identifier, T defaultValue, String displayName, String description, boolean editable, boolean hidden, boolean required, Type type, T min, T max) {
        super(identifier, defaultValue, displayName, description, editable, hidden, required);
        this.type = type;
        this.min = min;
        this.max = max;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public IComponent createWidget(IRenderable parent, float x, float y, float width, float gap, ISettingModule module) {
        NumberSettingWidget<T, NumberWidgetSetting<T>> widget = new NumberSettingWidget<>(x, y, width, gap, module, this);
        widget.setParent(parent);

        return widget;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}
