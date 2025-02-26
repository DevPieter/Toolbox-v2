package nl.devpieter.toolbox.Widgets.SettingWidgets;

import nl.devpieter.squid.Squid;
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
import nl.devpieter.toolbox.Widgets.Abstractions.SettingWidgetBase;
import nl.devpieter.toolbox.setting.widgetSettings.BooleanWidgetSetting;

import java.time.Duration;

public class BooleanSettingWidget extends SettingWidgetBase<Boolean, BooleanWidgetSetting> {

    public static final int HEIGHT = 26;

    private final float radius = 8;
    private final float knobPadding = 3;

    private final float gap;

    private final Animation slideAnimation;
    private final ColorAnimation slideColorAnimation;

    private final SCCard base;
    private final SCKnob knob;

    public BooleanSettingWidget(float x, float y, float width, float gap, ISettingModule module, BooleanWidgetSetting setting) {
        super(x, y, width, HEIGHT, gap, module, setting);

        this.gap = gap;

        this.slideAnimation = new Animation(0, 1, Duration.ofMillis(400), Easing.QUINT_OUT);
        this.slideColorAnimation = new ColorAnimation(Squid.THEME.container, Squid.THEME.primary, Duration.ofMillis(400), Easing.QUINT_OUT);

        STextStyle labelStyle = STextStyle.of().setFontSize(Squid.THEME.labelSize);
        SText labelText = SText.of(labelStyle).append(setting.getDisplayName());
        SCLabel label = new SCLabel(this, x, y, gap, this.height, labelText);
        label.alignment().set(Alignment.MIDDLE_LEFT).hoverable().setTrue();
        this.addChild(label);

        this.base = new SCCard(this, x + gap, y, 60, this.height);
        this.base.cornerRadius().set(this.radius);
        this.addChild(this.base);

        this.knob = new SCKnob(this, 0, y + knobPadding, this.height - (knobPadding * 2));
        this.knob.radiusAnimation().set(this.radius / 2, this.radius);
        this.addChild(this.knob);

        this.setState(setting.getValue(), false, false);
    }

    @Override
    protected void render(long context, float delta) {
        float knobX = this.x + this.gap + (this.base.getWidth() - this.height) * this.slideAnimation.get() + knobPadding;

        this.knob.setX(knobX);
        this.base.color().set(this.slideColorAnimation.get());
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!this.wasPressedInBounds() || !this.isHovered() || !this.isInteractable()) return false;

        this.setState(!this.setting.getValue(), true, true);
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
        this.slideColorAnimation.setTo(state ? Squid.THEME.primary : Squid.THEME.container);

        this.setting.setValue(state);
        if (save) this.save();
    }
}
