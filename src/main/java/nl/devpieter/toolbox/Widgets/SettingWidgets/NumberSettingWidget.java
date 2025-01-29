package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.components.SCKnob;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.components.SCNumberInputDisplay;
import nl.devpieter.squid.components.SCProgressBar;
import nl.devpieter.squid.layout.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Utils.NumberUtils;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;

public class NumberSettingWidget<T extends Number, S extends NumberWidgetSetting<T>> extends SettingWidgetBase<T, S> {

    public static final int HEIGHT = 26;

    private float displayWidth = 60;
    private float barThickness = 6;
    private float knobSize = 16;

    private SCLabel label;
    private SCProgressBar progressBar;
    private SCKnob knob;
    private SCNumberInputDisplay display;

    private SText labelText;
    private float gap;

    private T min;
    private T max;

    public NumberSettingWidget(float x, float y, float width, float gap, ISettingModule module, S setting, T min, T max) {
        super(x, y, width, HEIGHT, module, setting);

        this.labelText = SText.of().append(setting.getDisplayName());
        this.gap = gap;
        this.min = min;
        this.max = max;

        float halfHeight = this.height / 2;

        /* Label */
        STextStyle labelStyle = STextStyle.of().setFontSize(this.theme.labelSize);
        this.label = new SCLabel(this, this.x, this.y, this.gap, this.height, this.labelText);
        this.label.setAlignment(Alignment.MIDDLE_LEFT).setTextStyle(labelStyle).setHoverable(true);
        this.addChild(this.label);

        /* Progress Bar */
        float barX = this.x + this.gap;
        float barY = this.y + (halfHeight - (this.barThickness / 2));
        float barWidth = this.width - this.gap - this.knobSize - this.displayWidth;

        this.progressBar = new SCProgressBar(this, barX, barY, barWidth, this.barThickness);
        this.progressBar.setUserChangeAllowed(true).setScrollChangeAllowed(true).setParentScrollChangeAllowed(true);
        this.addChild(this.progressBar);

        /* Knob */
        float knobY = this.y + (halfHeight - (this.knobSize / 2));

        this.knob = new SCKnob(this, 0, knobY, this.knobSize);
        this.addChild(this.knob);

        /* Display */
        float displayX = this.x + this.width - this.displayWidth;
        int displayDigits = this.min instanceof Integer ? 0 : 2;

        this.display = new SCNumberInputDisplay(this, displayX, this.y, this.displayWidth, this.height);
        this.display.configureFormatter(displayDigits, displayDigits);
        this.addChild(this.display);

        this.setValue(this.setting.getValue(), false, false);
        this.progressBar.subscribeValueChangeListener(progress -> {
            float value = progress * (this.max.floatValue() - this.min.floatValue()) + this.min.floatValue();

            this.setting.setValue(NumberUtils.cast(this.setting.getType(), value));
            this.save();
        });
    }

    @Override
    protected void render(long context, float delta) {
        this.knob.setX(this.progressBar.getProgressX() - (this.knobSize / 2));

        float progress = this.progressBar.getAnimatedValue();
        float value = progress * (this.max.floatValue() - this.min.floatValue()) + this.min.floatValue();

        this.display.setValue(value);
    }

    public NumberSettingWidget<T, S> setValue(T value, boolean animate, boolean save) {
        float progress = (value.floatValue() - this.min.floatValue()) / (this.max.floatValue() - this.min.floatValue());
        this.progressBar.setValue(progress, animate);

        this.setting.setValue(value);
        if (save) this.save();

        return this;
    }
}
