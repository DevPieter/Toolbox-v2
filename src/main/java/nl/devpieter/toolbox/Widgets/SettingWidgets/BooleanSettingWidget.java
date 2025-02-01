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

    private final Animation slideAnimation;
    private final ColorAnimation slideColorAnimation;

    private SCLabel label;
    private SCCard base;
    private SCKnob knob;

    private SText labelText;
    private float gap;

    public BooleanSettingWidget(float x, float y, float width, float gap, ISettingModule module, BooleanWidgetSetting setting) {
        super(x, y, width, HEIGHT, module, setting);

        this.labelText = SText.of().append(setting.getDisplayName());
        this.gap = gap;

        this.slideAnimation = new Animation(0, 1, Duration.ofMillis(400), Easing.QUINT_OUT);
        this.slideColorAnimation = new ColorAnimation(Squid.THEME.container, Squid.THEME.primary, Duration.ofMillis(400), Easing.QUINT_OUT);

        float halfHeight = this.height / 2;

        /* Label */
        STextStyle labelStyle = STextStyle.of().setFontSize(Squid.THEME.labelSize);
        this.label = new SCLabel(this, this.x, this.y, this.gap, this.height, this.labelText);
        this.label.alignment().set(Alignment.MIDDLE_LEFT).text().setStyle(labelStyle).hoverable().setTrue();
        this.addChild(this.label);

        /* Base */
        float baseX = this.x + this.gap;
        float baseY = this.y + (halfHeight - (this.height / 2));
        float baseWidth = this.width - this.gap;

        this.base = new SCCard(this, baseX, baseY, baseWidth, this.height);
        this.base.cornerRadius().set(this.radius);
        this.addChild(this.base);

        /* Knob */
        float knobY = this.y + knobPadding;

        this.knob = new SCKnob(this, 0, knobY, this.height - (knobPadding * 2));
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
    public void mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!this.wasPressedInBounds() || !this.isHovered() || !this.isInteractable()) return;

        this.setState(!this.setting.getValue(), true, true);
    }

    public BooleanSettingWidget setState(boolean state, boolean animate, boolean save) {

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

        return this;
    }
}
