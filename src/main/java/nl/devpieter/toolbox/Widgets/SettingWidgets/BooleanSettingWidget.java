package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.animation.ColorAnimation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCKnob;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.interfaces.IBooleanSetting;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.awt.*;
import java.time.Duration;

public class BooleanSettingWidget extends SettingWidgetBase<Boolean, IWidgetSetting<Boolean, IBooleanSetting>> {

    public static final int HEIGHT = 26;

    private final Color bodyColor = colorManager.get("setting.generic.body");
    private final Color bodyEnabledColor = colorManager.get("setting.generic.body.enabled");

    // TODO - Get from Theme
    private final float cornerRadius = 8;
    private final float knobPadding = 4;

    private final Animation slideAnimation;
    private final ColorAnimation slideColorAnimation;

    private final SCCard body;
    private final SCKnob knob;

    public BooleanSettingWidget(float x, float y, float width, float gap, ISettingModule module, IWidgetSetting<Boolean, IBooleanSetting> widgetSetting) {
        super(x, y, width, HEIGHT, gap, module, widgetSetting);

        this.slideAnimation = new Animation(0, 1, Duration.ofMillis(400), Easing.EXPO_OUT);
        this.slideColorAnimation = new ColorAnimation(bodyColor, bodyEnabledColor, Duration.ofMillis(400), Easing.EXPO_OUT);

        this.body = new SCCard(this, x + gap, y, 60, height);
        this.body.cornerRadius().set(this.cornerRadius);
        this.addChild(this.body);

        this.knob = new SCKnob(this.body, 0, y + this.knobPadding, height - (this.knobPadding * 2));
        this.knob.color().set(colorManager.get("setting.generic.knob"), colorManager.get("setting.generic.knob.hover")).radiusAnimation().set(this.cornerRadius / 2, this.cornerRadius);
        this.body.addChild(this.knob);

        this.setState(this.widgetSetting.getSetting().getValue(), false, false);
    }

    @Override
    protected void render(long context, float delta) {
        this.knob.setX(this.x + this.gap + this.knobPadding + (this.body.getWidth() - this.knob.getWidth() - 2 * this.knobPadding) * this.slideAnimation.get());
        this.body.color().set(this.slideColorAnimation.get());
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
        this.slideColorAnimation.setTo(state ? this.bodyEnabledColor : this.bodyColor);

        this.widgetSetting.getSetting().setValue(state);
        if (save) this.save();
    }
}
