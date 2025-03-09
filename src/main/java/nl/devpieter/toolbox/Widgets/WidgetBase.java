package nl.devpieter.toolbox.Widgets;

import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.toolbox.Category.CategoryManager;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.Theme.FontManager;
import nl.devpieter.toolbox.Theme.SvgManager;

public abstract class WidgetBase extends ComponentBase {

    protected final CategoryManager categoryManager = CategoryManager.getInstance();
    protected final ModuleManager moduleManager = ModuleManager.getInstance();
    protected final ColorManager colorManager = ColorManager.getInstance();
    protected final FontManager fontManager = FontManager.getInstance();
    protected final SvgManager svgManager = SvgManager.getInstance();

    public WidgetBase(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
}
