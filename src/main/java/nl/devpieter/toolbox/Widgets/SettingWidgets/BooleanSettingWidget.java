package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCKnob;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.modifiers.hoverable.HoverableColorModifier;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.interfaces.IBooleanSetting;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.awt.*;
import java.time.Duration;

public class BooleanSettingWidget extends SettingWidgetBase<Boolean, IWidgetSetting<Boolean, IBooleanSetting>> {

    public static final int HEIGHT = 26;

    private final Color bodyColor = colorManager.get("setting.generic.body");
    private final Color bodyHoverColor = colorManager.get("setting.generic.body.hover");
    private final Color bodyEnabledColor = colorManager.get("setting.generic.body.enabled");
    private final Color bodyEnabledHoverColor = colorManager.get("setting.generic.body.enabled.hover");

    // TODO - Get from Theme
    private final float cornerRadius = 8;
    private final float knobPadding = 4;

    private final Animation slideAnimation;
    private final HoverableColorModifier<BooleanSettingWidget> bodyColorModifier = new HoverableColorModifier<>(this);

    private final SCCard body;
    private final SCKnob knob;

    public BooleanSettingWidget(float x, float y, float width, float gap, ISettingModule module, IWidgetSetting<Boolean, IBooleanSetting> widgetSetting) {
        super(x, y, width, HEIGHT, gap, module, widgetSetting);

        this.slideAnimation = new Animation(0, 1, Duration.ofMillis(400), Easing.EXPO_OUT);
        this.bodyColorModifier.setDuration(Duration.ofMillis(400));
        this.bodyColorModifier.setEasing(Easing.EXPO_OUT);
        this.bodyColorModifier.setSmoothBackAndForth(true);

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
        this.bodyColorModifier.tickHoverCheck(this.body.isHovered());

        this.knob.setX(this.x + this.gap + this.knobPadding + (this.body.getWidth() - this.knob.getWidth() - 2 * this.knobPadding) * this.slideAnimation.get());
        this.body.color().set(this.bodyColorModifier.get());
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

//            this.bodyColorModifier.setFrom(this.bodyColorModifier.get());
//            this.bodyColorModifier.reset();
        }

        this.slideAnimation.setTo(state ? 1f : 0);
//        this.bodyColorModifier.setTo(state ? this.bodyEnabledColor : this.bodyColor);

        this.widgetSetting.getSetting().setValue(state);
        if (save) this.save();

        this.updateBodyColor(!animate);
    }

    private void updateBodyColor(boolean instant) {
        boolean isEnabled = this.widgetSetting.getSetting().getValue();

        Color color = isEnabled ? this.bodyEnabledColor : this.bodyColor;
        Color hoverColor = isEnabled ? this.bodyEnabledHoverColor : this.bodyHoverColor;

        if (instant) this.bodyColorModifier.set(color, hoverColor);
        else this.bodyColorModifier.transition(color, hoverColor);
    }
}
