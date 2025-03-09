package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.animation.ColorAnimation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCKnob;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.interfaces.IBooleanSetting;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.awt.*;
import java.time.Duration;

public class BooleanSettingWidget extends SettingWidgetBase<Boolean, IWidgetSetting<Boolean, IBooleanSetting>> {

    public static final int HEIGHT = 26;

    // TODO - Change to correct colors
    private final Color baseColor = Color.DARK_GRAY;
    private final Color baseEnabledColor = colorManager.get("module.card.title.enabled");
    private final Color knobColor = Color.WHITE;

    // TODO - Get from Theme
    private final float cornerRadius = 8;
    private final float knobPadding = 4;

    private final Animation slideAnimation;
    private final ColorAnimation slideColorAnimation;

    private final SCCard base;
    private final SCKnob knob;

    public BooleanSettingWidget(float x, float y, float width, float gap, ISettingModule module, IWidgetSetting<Boolean, IBooleanSetting> widgetSetting) {
        super(x, y, width, HEIGHT, gap, module, widgetSetting);

        this.slideAnimation = new Animation(0, 1, Duration.ofMillis(400), Easing.EXPO_OUT);
        this.slideColorAnimation = new ColorAnimation(this.baseColor, this.baseEnabledColor, Duration.ofMillis(400), Easing.EXPO_OUT);

        this.base = new SCCard(this, x + gap, y, 60, height);
        this.base.cornerRadius().set(this.cornerRadius);
        this.addChild(this.base);

        this.knob = new SCKnob(this.base, 0, y + this.knobPadding, height - (this.knobPadding * 2));
        this.knob.color().set(knobColor).radiusAnimation().set(this.cornerRadius / 2, this.cornerRadius);
        this.addChild(this.knob);

        this.setState(this.widgetSetting.getSetting().getValue(), false, false);
    }

    @Override
    protected void render(long context, float delta) {
        this.knob.setX(this.x + this.gap + this.knobPadding + (this.base.getWidth() - this.knob.getWidth() - 2 * this.knobPadding) * this.slideAnimation.get());
        this.base.color().set(this.slideColorAnimation.get());
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!this.wasPressedInBounds() || !this.isHovered() || !this.isInteractable()) return false;

        this.setState(!this.widgetSetting.getSetting().getValue(), true, true);
        return true;
    }

    private void setState(boolean state, boolean animate, boolean save) {

        if (animate) {
            this.slideAnimation.setFrom(this.slideAnimation.get());
            this.slideAnimation.reset();

            this.slideColorAnimation.setFrom(this.slideColorAnimation.get());
            this.slideColorAnimation.reset();
        }

        this.slideAnimation.setTo(state ? 1f : 0);
        this.slideColorAnimation.setTo(state ? this.baseEnabledColor : this.baseColor);

        this.widgetSetting.getSetting().setValue(state);
        if (save) this.save();
    }
}
