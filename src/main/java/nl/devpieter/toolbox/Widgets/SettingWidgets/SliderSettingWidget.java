package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.components.SCNumberInputDisplay;
import nl.devpieter.squid.components.SCProgressBar;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Utils.NumberUtils;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.base.WidgetSettingBase;
import nl.devpieter.toolbox.setting.interfaces.IClampedSetting;

import java.awt.*;

public class SliderSettingWidget<T extends Number, S extends WidgetSettingBase<T, IClampedSetting<T>>> extends SettingWidgetBase<T, S> {

    public static final int HEIGHT = 26;

    // TODO - Change to correct colors
    private final Color baseColor = Color.DARK_GRAY;
    private final Color progressColor = colorManager.get("module.card.title.enabled");
    private final Color knobColor = Color.WHITE;

    // TODO - Get from Theme
    private final float displayWidth = 60;
    private final float barThickness = 6;
    private final float knobSize = 16;

    private final T min;
    private final T max;

    private SCProgressBar bar;
    private SCNumberInputDisplay display;

    public SliderSettingWidget(float x, float y, float width, float gap, ISettingModule module, S widgetSetting) {
        super(x, y, width, HEIGHT, gap, module, widgetSetting);

        this.min = this.widgetSetting.getSetting().getMin();
        this.max = this.widgetSetting.getSetting().getMax();

        this.bar = new SCProgressBar(this, x + gap, y + (height / 2 - (this.barThickness / 2)), width - gap - this.knobSize - this.displayWidth, this.barThickness);
        this.bar.userChangeAllowed().setTrue().scrollChangeAllowed().setTrue().parentScrollChangeAllowed().setTrue();
        this.bar.containerColor().set(this.baseColor).progressBarColor().set(this.progressColor);
        this.addChild(this.bar);

        int displayDigits = min instanceof Integer ? 0 : 2;
        this.display = new SCNumberInputDisplay(this, x + width - this.displayWidth, y, this.displayWidth, height);
        this.display.containerColor().set(this.baseColor).formatter().setFractionDigits(displayDigits, displayDigits);
        this.addChild(this.display);

        this.setValue(this.widgetSetting.getSetting().getValue(), false, false);

        bar.subscribeValueChangeListener(progress -> {
            float value = progress * (this.max.floatValue() - this.min.floatValue()) + this.min.floatValue();

            this.widgetSetting.getSetting().setValue(NumberUtils.cast(this.widgetSetting.getSetting().getType(), value));
            this.save();
        });
    }

    @Override
    protected void render(long context, float delta) {
//        this.knob.setX(this.progressBar.getProgressX() - (this.knobSize / 2));

        float progress = this.bar.getAnimatedValue();
        float value = progress * (this.max.floatValue() - this.min.floatValue()) + this.min.floatValue();

        this.display.setValue(value);
    }

    private void setValue(T value, boolean animate, boolean save) {
        float progress = (value.floatValue() - this.min.floatValue()) / (this.max.floatValue() - this.min.floatValue());
        this.bar.setValue(progress, animate);

        this.widgetSetting.getSetting().setValue(value);
        if (save) this.save();
    }
}
