package nl.devpieter.toolbox.setting.widgetSettings;

import nl.devpieter.squid.renderable.component.IComponent;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.SettingWidgets.SliderSettingWidget;
import nl.devpieter.toolbox.setting.base.WidgetSettingBase;
import nl.devpieter.toolbox.setting.interfaces.IClampedSetting;

public class NumberWidgetSetting<T extends Number> extends WidgetSettingBase<T, IClampedSetting<T>> {

    public enum WidgetType {
        SLIDER
    }

    private final WidgetType type;

    public NumberWidgetSetting(String name, String description, WidgetType type, IClampedSetting<T> setting) {
        super(name, description, setting);
        this.type = type;
    }

    public NumberWidgetSetting(String name, String description, WidgetType type, IClampedSetting<T> setting, boolean editable, boolean hidden, boolean required) {
        super(name, description, setting, editable, hidden, required);
        this.type = type;
    }

    @Override
    public IComponent createWidget(float x, float y, float width, float gap, ISettingModule module) {
        return switch (type) {
            case SLIDER -> new SliderSettingWidget<>(x, y, width, gap, module, this);
        };
    }
}
