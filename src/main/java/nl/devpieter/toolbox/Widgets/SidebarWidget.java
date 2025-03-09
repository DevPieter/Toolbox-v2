package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.animation.Animation;
import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.enums.Easing;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Toolbox;

import java.time.Duration;

public class SidebarWidget extends WidgetBase {

    private final int shadowColor = colorManager.getRGB("sidebar.shadow");

    private final Animation slideAnimation;

    public SidebarWidget(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.slideAnimation = new Animation(-width, 0, Duration.ofMillis(300), Easing.QUINT_OUT, true);

        SCCard sidebar = new SCCard(this, x, y, width, height);
        sidebar.color().set(colorManager.get("sidebar.body")).cornerRadius().set(0, 16, 16, 0);
        this.addChild(sidebar);

        STextStyle titleStyle = STextStyle.of(colorManager.get("sidebar.body.title"), fontManager.get("kaushan_script"), 96);
        SCLabel title = new SCLabel(sidebar, x, y + 40, width, 96, SText.of("Toolbox", titleStyle));
        title.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(title);

        STextStyle subtitleStyle = STextStyle.of(colorManager.get("sidebar.body.subtitle"), fontManager.get("kaushan_script"), 18);
        SCLabel subtitle = new SCLabel(sidebar, x, y + 140, width - 120, 18, SText.of("By DevPieter", subtitleStyle));
        subtitle.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(subtitle);

        String versionText = String.format("v%s / %s (v%s) - %s", Toolbox.getToolboxVersion(), Toolbox.getMinecraftVersion(), Toolbox.getSquidVersion(), Toolbox.getBuildStatus());
        STextStyle versionStyle = STextStyle.of(colorManager.get("sidebar.body.version"), 14);
        SCLabel version = new SCLabel(sidebar, x, height - 8, width, 16, SText.of(versionText, versionStyle));
        version.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(version);
    }

    @Override
    protected void render(long context, float delta) {
        NVGHelper.translate(context, this.slideAnimation.get(), 0);

        NVGHelper.drawDropShadow(
                context,
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                8,
                8,
                16,
                this.shadowColor
        );
    }
}
