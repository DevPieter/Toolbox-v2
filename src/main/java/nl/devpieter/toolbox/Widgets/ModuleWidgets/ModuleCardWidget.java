package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.components.SCSvg;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.svg.SvgHolder;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.*;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Theme.SvgManager;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModuleCardWidget extends ComponentBase {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 145;

    private final ModuleManager moduleManager = ModuleManager.getInstance();
    private final ColorManager colorManager = ColorManager.getInstance();
    private final SvgManager svgManager = SvgManager.getInstance();

    private final Color titleColor = colorManager.get("module.card.title");
    private final Color titleEnabledColor = colorManager.get("module.card.title.enabled");
    private final int shadowColor = colorManager.getRGB("module.card.shadow");
    private final int shadowEnabledColor = colorManager.getRGB("module.card.shadow.enabled");

    private final Class<? extends IModule> moduleClass;
    private final boolean isInteractable, isClickable, isToggleable, hasSettings;

    private final SCLabel title;
    private final SCSvg favorite;
    private final SCSvg settings;

    public ModuleCardWidget(IRenderable parent, float x, float y, Class<? extends IModule> moduleClass) {
        super(x, y, WIDTH, HEIGHT);
        this.setParent(parent);

        this.moduleClass = moduleClass;
        isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
        isClickable = IClickableModule.class.isAssignableFrom(moduleClass);
        isToggleable = IToggleableModule.class.isAssignableFrom(moduleClass);
        hasSettings = ISettingModule.class.isAssignableFrom(moduleClass);

        String nameText = moduleManager.getName(moduleClass).getString();
        String descriptionText = moduleManager.getDescription(moduleClass).getString();

        Color topColor = colorManager.get("module.card.top");
        Color bottomColor = colorManager.get("module.card.bottom");
        Color descriptionColor = colorManager.get("module.card.description");

        Color favoriteIconColor = colorManager.get("module.card.favorite.icon");
        Color favoriteIconHoverColor = colorManager.get("module.card.favorite.icon.hover");
        Color favoriteIconFavoriteColor = colorManager.get("module.card.favorite.icon.favorite");

        Color settingsIconColor = colorManager.get("module.card.settings.icon");
        Color settingsIconHoverColor = colorManager.get("module.card.settings.icon.hover");

        SvgHolder favoriteSvg = svgManager.get("icons.favorite");
        SvgHolder settingSvg = svgManager.get("icons.settings");
        float svgSize = 24;

        SCCard cardTop = new SCCard(this, x, y, this.width, 50);
        cardTop.color().set(topColor).cornerRadius().set(8, 8, 0, 0);
        addChild(cardTop);

        SCCard cardBottom = new SCCard(this, x, y + 40, width, height - 40);
        cardBottom.color().set(bottomColor).cornerRadius().set(12, 12, 8, 8);
        addChild(cardBottom);

        STextStyle titleStyle = STextStyle.of(titleColor).setBold(true).setFontSize(24);
        title = new SCLabel(this, x + 12, y, width - 24, 40, SText.of(titleStyle).append(nameText));
        title.alignment().set(Alignment.MIDDLE_LEFT);
        addChild(title);

        this.favorite = new SCSvg(this, x + width - svgSize - 12, y + 8, svgSize, svgSize, favoriteSvg);
        this.favorite.color().set(favoriteIconColor, favoriteIconHoverColor).inheritParentHover().setFalse();
        addChild(this.favorite);

        this.settings = new SCSvg(this, x + width - svgSize - 12 - svgSize - 6, y + 8, svgSize, svgSize, settingSvg);
        this.settings.color().set(settingsIconColor, settingsIconHoverColor).inheritParentHover().setFalse();
        if (this.hasSettings) addChild(this.settings);

        STextStyle descriptionStyle = STextStyle.of(descriptionColor).setFontSize(18);
        SCLabel description = new SCLabel(this, x + 12, y + 46, width - 24, height - 46, SText.of(descriptionStyle).append(descriptionText));
        description.multiLine().setTrue();
        addChild(description);
    }

    @Override
    protected void render(long context, float delta) {
        title.text().getStyle().setColor(moduleManager.isEnabled(moduleClass) ? titleEnabledColor : titleColor);

        int color = moduleManager.isEnabled(moduleClass) ? shadowEnabledColor : shadowColor;
        NVGHelper.drawDropShadow(context, getX() + 12, getY() + 12, getWidth() - 16, getHeight() - 16, 12, 6, 4, color);
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!wasPressedInBounds() || !isHovered() || !isInteractable()) return false;

        if (this.favorite.isHovered() && this.favorite.wasPressedInBounds()) {
            return true;
        } else if (this.settings.isHovered() && this.settings.wasPressedInBounds()) {
            return this.moduleManager.openSettings(moduleClass);
        }

        if (isClickable) if (moduleManager.click(moduleClass, button)) return true;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isToggleable) {
            moduleManager.toggle(moduleClass);
            return true;
        }

        return false;
    }
}
