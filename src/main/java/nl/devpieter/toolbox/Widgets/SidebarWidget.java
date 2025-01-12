package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.layout.Alignment;
import nl.devpieter.squid.renderable.widget.WidgetBase;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.animation.Animation;
import nl.devpieter.squid.rendering.animation.Easing;
import nl.devpieter.squid.rendering.font.Fonts;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Toolbox;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.time.Duration;

public class SidebarWidget extends WidgetBase {

    private final ColorManager colorManager = ColorManager.getInstance();
    private final int shadowColor = this.colorManager.getRGB("sidebar.shadow");

    private final Animation slideAnimation;

    public SidebarWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

//        Color backgroundColor = new Color(9, 9, 9);
//        Color titleColor = new Color(214, 217, 219);
//        Color subtitleColor = new Color(118, 118, 118);

        Color backgroundColor = colorManager.get("sidebar.background");
        Color titleColor = colorManager.get("sidebar.title");
        Color subtitleColor = colorManager.get("sidebar.subtitle");
        Color versionColor = colorManager.get("sidebar.version");

        slideAnimation = new Animation(-width, 0, Duration.ofMillis(300), Easing.QUINT_OUT, true);

        SCCard sidebar = new SCCard(this, x, y, width, height);
        sidebar.color().set(backgroundColor).cornerRadius().set(0, 16, 16, 0);
        addChild(sidebar);

        STextStyle titleStyle = STextStyle.of(titleColor).setFont(Toolbox.KAUSHAN_SCRIPT).setFontSize(96);
        SCLabel title = new SCLabel(this, x, y + 40, width, 96, SText.of(titleStyle).append("Toolbox"));
        title.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(title);

        STextStyle subtitleStyle = STextStyle.of(subtitleColor).setFont(Toolbox.KAUSHAN_SCRIPT).setFontSize(18);
        SCLabel subtitle = new SCLabel(this, x, y + 140, width - 120, 18, SText.of(subtitleStyle).append("By DevPieter"));
        subtitle.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(subtitle);

        STextStyle versionStyle = STextStyle.of(versionColor).setFont(Fonts.CALIBRI).setFontSize(16);
        SCLabel version = new SCLabel(this, x, height - 18, width, 16, SText.of(versionStyle).append(String.format("v%s / %s - %s", Toolbox.getToolboxVersion(), Toolbox.getMinecraftVersion(), Toolbox.getBuildStatus())));
        version.setAlignment(Alignment.MIDDLE_CENTER);
        addChild(version);
    }

    @Override
    protected void render(long context, float delta) {
        NanoVG.nvgTranslate(context, this.slideAnimation.get(), 0);

//        NVGHelper.drawDropShadow(context, getX(), getY(), getWidth(), getHeight(), 8, 8, 16, 0x33000000);
        NVGHelper.drawDropShadow(context, getX(), getY(), getWidth(), getHeight(), 8, 8, 16, this.shadowColor);
    }
}
