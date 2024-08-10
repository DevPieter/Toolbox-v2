package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.layout.Alignment;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.animation.Animation;
import nl.devpieter.squid.rendering.animation.Easing;
import nl.devpieter.squid.rendering.font.Fonts;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Toolbox;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.time.Duration;

public class SidebarWidget extends WidgetBase {

    private final Animation slideAnimation;

    public SidebarWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        Color background = new Color(26, 27, 30);
        Color text = new Color(255, 255, 255);
        Color gray = new Color(118, 118, 118);

        this.slideAnimation = new Animation(-width, 0, Duration.ofMillis(400), Easing.EXPO_IN_OUT, true);

        SCCard sidebar = new SCCard(this, x, y, width, height);
        sidebar.setColor(background);
        this.addChild(sidebar);

        STextStyle titleStyle = STextStyle.of().setFont(Toolbox.KAUSHAN_SCRIPT).setFontSize(96);
        SCLabel title = new SCLabel(this, x, y + 40, width, 96, SText.of(titleStyle).append("Toolbox"));
        title.setColor(text).setAlignment(Alignment.MIDDLE_CENTER);
        this.addChild(title);

        STextStyle subtitleStyle = STextStyle.of().setFont(Toolbox.KAUSHAN_SCRIPT).setFontSize(18);
        SCLabel subtitle = new SCLabel(this, x, y + 140, width - 120, 18, SText.of(subtitleStyle).append("By DevPieter"));
        subtitle.setColor(gray).setAlignment(Alignment.MIDDLE_CENTER);
        this.addChild(subtitle);

        STextStyle versionStyle = STextStyle.of().setFont(Fonts.CALIBRI).setFontSize(16);
        SCLabel version = new SCLabel(this, x, height - 18, width, 16, SText.of(versionStyle).append(String.format("v%s / %s - %s", Toolbox.MOD_VERSION, Toolbox.GAME_VERSION, Toolbox.MOD_STATUS)));
        version.setColor(gray).setAlignment(Alignment.MIDDLE_CENTER);
        this.addChild(version);
    }

    @Override
    protected void render(long context, float delta) {
        NanoVG.nvgTranslate(context, this.slideAnimation.get(), 0);
    }
}
