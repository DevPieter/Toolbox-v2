package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.components.SCSvg;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.*;
import nl.devpieter.toolbox.Widgets.WidgetBase;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModuleCardWidget extends WidgetBase {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 145;

    private final Color titleColor = colorManager.get("module.card.header.text");
    private final Color titleEnabledColor = colorManager.get("module.card.header.text.enabled");
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
        this.isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
        this.isClickable = IClickableModule.class.isAssignableFrom(moduleClass);
        this.isToggleable = IToggleableModule.class.isAssignableFrom(moduleClass);
        this.hasSettings = ISettingModule.class.isAssignableFrom(moduleClass);

        String titleText = moduleManager.getName(moduleClass).getString();
        String descriptionText = moduleManager.getDescription(moduleClass).getString();

        float svgSize = 24;

        /* Header */
        SCCard cardHeader = new SCCard(this, x, y, width, 50);
        cardHeader.color().set(colorManager.get("module.card.header")).cornerRadius().set(8, 8, 0, 0);
        this.addChild(cardHeader);

        STextStyle titleStyle = STextStyle.of(this.titleColor, 24).setBold();
        this.title = new SCLabel(cardHeader, x + 12, y, width - svgSize - 24 - (this.hasSettings ? svgSize + 6 : 0) - 6, 40, SText.of(titleText, titleStyle));
        this.title.alignment().set(Alignment.MIDDLE_LEFT);
        cardHeader.addChild(this.title);

        this.favorite = new SCSvg(cardHeader, x + width - svgSize - 12, y + 8, svgSize, svgSize, svgManager.get("icons.favorite"));
        this.favorite.color().set(colorManager.get("module.card.header.icon.favorite"), colorManager.get("module.card.header.icon.favorite.hover")).inheritParentHover().setFalse();
        cardHeader.addChild(this.favorite);

        this.settings = new SCSvg(cardHeader, x + width - svgSize - 12 - svgSize - 6, y + 8, svgSize, svgSize, svgManager.get("icons.settings"));
        this.settings.color().set(colorManager.get("module.card.header.icon.settings"), colorManager.get("module.card.header.icon.settings.hover")).inheritParentHover().setFalse();
        if (this.hasSettings) cardHeader.addChild(this.settings);

        /* Body */
        SCCard cardBody = new SCCard(this, x, y + 40, width, height - 40);
        cardBody.color().set(colorManager.get("module.card.body")).cornerRadius().set(12, 12, 8, 8);
        this.addChild(cardBody);

        STextStyle descriptionStyle = STextStyle.of(colorManager.get("module.card.body.text"), 18);
        SCLabel description = new SCLabel(cardBody, x + 12, y + 46, width - 24, height - 46, SText.of(descriptionText, descriptionStyle));
        description.multiLine().setTrue();
        cardBody.addChild(description);
    }

    @Override
    protected void render(long context, float delta) {
        this.title.text().getStyle().setColor(this.moduleManager.isEnabled(this.moduleClass) ? this.titleEnabledColor : this.titleColor);
        int color = this.moduleManager.isEnabled(this.moduleClass) ? this.shadowEnabledColor : this.shadowColor;

        NVGHelper.drawDropShadow(
                context,
                this.getX() + 12,
                this.getY() + 12,
                this.getWidth() - 16,
                this.getHeight() - 16,
                12,
                6,
                4,
                color
        );
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
