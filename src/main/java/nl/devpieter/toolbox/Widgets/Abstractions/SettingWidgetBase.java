package nl.devpieter.toolbox.Widgets.Abstractions;

import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Theme.ColorManager;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.awt.*;

public abstract class SettingWidgetBase<T, S extends IWidgetSetting<T, ?>> extends ComponentBase {

    protected final SettingManager settingManager = SettingManager.getInstance();
    protected final ColorManager colorManager = ColorManager.getInstance();

    // TODO - Change to correct colors
    private final Color titleColor = colorManager.get("module.card.description");
    private final Color requiredColor = Color.RED;

    protected final float gap;

    protected final ISettingModule module;
    protected final S widgetSetting;

    public SettingWidgetBase(float x, float y, float width, float height, float gap, ISettingModule module, S widgetSetting) {
        super(x, y, width, height);

        this.gap = gap;
        this.module = module;
        this.widgetSetting = widgetSetting;

        STextStyle titleStyle = STextStyle.of(titleColor).setFontSize(18);
        SText titleText = SText.of(titleStyle).append(widgetSetting.getName());

        if (widgetSetting.isRequired()) {
            STextStyle requiredStyle = STextStyle.of(requiredColor).setFontSize(16).setBold(true);
            titleText.append("*", requiredStyle);
        }

        SCLabel title = new SCLabel(this, x, y, gap, height, titleText);
        title.alignment().set(Alignment.MIDDLE_LEFT);
        this.addChild(title);

        this.setDisabled(!widgetSetting.isEditable());
    }

    public void save() {
        this.settingManager.saveSetting(module, widgetSetting.getSetting());
    }
}
