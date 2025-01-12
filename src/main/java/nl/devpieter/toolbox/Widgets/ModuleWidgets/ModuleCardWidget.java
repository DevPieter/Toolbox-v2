package nl.devpieter.toolbox.Widgets.ModuleWidgets;

import nl.devpieter.squid.components.SCCard;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.layout.Alignment;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.NVGHelper;
import nl.devpieter.squid.rendering.font.Fonts;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.IInteractableModule;
import nl.devpieter.toolbox.Module.Interfaces.IModule;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Theme.ColorManager;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModuleCardWidget extends ComponentBase {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 145;

    private final ModuleManager moduleManager = ModuleManager.getInstance();
    private final ColorManager colorManager = ColorManager.getInstance();

    private final Color titleColor = colorManager.get("module.card.title");
    private final Color titleEnabledColor = colorManager.get("module.card.title.enabled");
    private final int shadowColor = colorManager.getRGB("module.card.shadow");
    private final int shadowEnabledColor = colorManager.getRGB("module.card.shadow.enabled");

    private final Class<? extends IModule> moduleClass;
    private final boolean isInteractable, isClickable, isToggleable;

    private final SCLabel title;

    public ModuleCardWidget(IRenderable parent, float x, float y, Class<? extends IModule> moduleClass) {
        super(parent, x, y, WIDTH, HEIGHT);

        this.moduleClass = moduleClass;
        isInteractable = IInteractableModule.class.isAssignableFrom(moduleClass);
        isClickable = IClickableModule.class.isAssignableFrom(moduleClass);
        isToggleable = IToggleableModule.class.isAssignableFrom(moduleClass);

        String nameText = moduleManager.getName(moduleClass).getString();
        String descriptionText = moduleManager.getDescription(moduleClass).getString();

//        Color topColor = new Color(9, 9, 9);
//        Color bottomColor = new Color(17, 17, 17);
//        Color text = new Color(214, 217, 219);
//        Color titleColor = new Color(118, 118, 118);

        Color topColor = colorManager.get("module.card.top");
        Color bottomColor = colorManager.get("module.card.bottom");
        Color descriptionColor = colorManager.get("module.card.description");

        SCCard cardTop = new SCCard(this, x, y, this.width, 50);
        cardTop.color().set(topColor).cornerRadius().set(8, 8, 0, 0);
        addChild(cardTop);

        SCCard cardBottom = new SCCard(this, x, y + 40, width, height - 40);
        cardBottom.color().set(bottomColor).cornerRadius().set(12, 12, 8, 8);
        addChild(cardBottom);

        STextStyle titleStyle = STextStyle.of(titleColor).setFont(Fonts.CALIBRI).setBold(true).setFontSize(24);
        title = new SCLabel(this, x + 12, y, width - 24, 40, SText.of(titleStyle).append(nameText));
        title.setAlignment(Alignment.MIDDLE_LEFT);
        addChild(title);

        STextStyle descriptionStyle = STextStyle.of(descriptionColor).setFont(Fonts.CALIBRI).setFontSize(18);
        SCLabel description = new SCLabel(this, x + 12, y + 46, width - 24, height - 46, SText.of(descriptionStyle).append(descriptionText));
        description.setMultiLine(true);
        addChild(description);
    }

    @Override
    protected void render(long context, float delta) {
        title.textStyle().setColor(moduleManager.isEnabled(moduleClass) ? titleEnabledColor : titleColor);

        int color = moduleManager.isEnabled(moduleClass) ? shadowEnabledColor : shadowColor;
        NVGHelper.drawDropShadow(context, getX() + 12, getY() + 12, getWidth() - 16, getHeight() - 16, 12, 6, 4, color);
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (!wasPressedInBounds() || !isHovered() || !isInteractable()) return;

        if (isClickable) if (moduleManager.click(moduleClass, button)) return;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isToggleable) {
            moduleManager.toggle(moduleClass);
        }
    }
}
