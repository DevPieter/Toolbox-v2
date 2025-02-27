package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.Squid;
import nl.devpieter.squid.components.SCKnob;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.components.SCNumberInputDisplay;
import nl.devpieter.squid.components.SCProgressBar;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Utils.NumberUtils;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;

public class SliderSettingWidget<T extends Number, S extends NumberWidgetSetting<T>> extends SettingWidgetBase<T, S> {

    public static final int HEIGHT = 26;

    private float displayWidth = 60;
    private float barThickness = 6;
    private float knobSize = 16;

    private SCLabel label;
    private SCProgressBar progressBar;
    private SCKnob knob;
    private SCNumberInputDisplay display;

    private SText labelText;

    private final T min;
    private final T max;


    public SliderSettingWidget(float x, float y, float width, float gap, ISettingModule module, S widgetSetting) {
        super(x, y, width, HEIGHT, gap, module, widgetSetting);

        this.labelText = SText.of().append(widgetSetting.getName());
        this.min = this.widgetSetting.getSetting().getMin();
        this.max = this.widgetSetting.getSetting().getMax();

        float halfHeight = this.height / 2;

        /* Label */
        STextStyle labelStyle = STextStyle.of().setFontSize(Squid.THEME.labelSize);
        this.label = new SCLabel(this, this.x, this.y, gap, this.height, this.labelText);
        this.label.alignment().set(Alignment.MIDDLE_LEFT).text().setStyle(labelStyle).hoverable().setTrue();
        this.addChild(this.label);

        /* Progress Bar */
        float barX = this.x + gap;
        float barY = this.y + (halfHeight - (this.barThickness / 2));
        float barWidth = this.width - gap - this.knobSize - this.displayWidth;

        this.progressBar = new SCProgressBar(this, barX, barY, barWidth, this.barThickness);
        this.progressBar.userChangeAllowed().setTrue().scrollChangeAllowed().setTrue().parentScrollChangeAllowed().setTrue();
        this.addChild(this.progressBar);

        /* Knob */
        float knobY = this.y + (halfHeight - (this.knobSize / 2));

        this.knob = new SCKnob(this, 0, knobY, this.knobSize);
        this.knob.radiusAnimation().set(4, 8);
        this.addChild(this.knob);

        /* Display */
        float displayX = this.x + this.width - this.displayWidth;
        int displayDigits = this.min instanceof Integer ? 0 : 2;

        this.display = new SCNumberInputDisplay(this, displayX, this.y, this.displayWidth, this.height);
        this.display.formatter().setFractionDigits(displayDigits, displayDigits);
        this.addChild(this.display);

        this.setValue(this.widgetSetting.getSetting().getValue(), false, false);
        this.progressBar.subscribeValueChangeListener(progress -> {
            float value = progress * (this.max.floatValue() - this.min.floatValue()) + this.min.floatValue();

            this.widgetSetting.getSetting().setValue(NumberUtils.cast(this.widgetSetting.getSetting().getType(), value));
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

    public SliderSettingWidget<T, S> setValue(T value, boolean animate, boolean save) {
        float progress = (value.floatValue() - this.min.floatValue()) / (this.max.floatValue() - this.min.floatValue());
        this.progressBar.setValue(progress, animate);

        this.widgetSetting.getSetting().setValue(value);
        if (save) this.save();

        return this;
    }
}
