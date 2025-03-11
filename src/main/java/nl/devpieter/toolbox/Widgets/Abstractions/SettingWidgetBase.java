package nl.devpieter.toolbox.Widgets.Abstractions;

import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Utils.ConfigUtils;
import nl.devpieter.toolbox.Widgets.WidgetBase;
import nl.devpieter.toolbox.setting.SettingManager;
import nl.devpieter.toolbox.setting.interfaces.IWidgetSetting;

import java.io.File;

public abstract class SettingWidgetBase<T, S extends IWidgetSetting<T, ?>> extends WidgetBase {

    protected final SettingManager settingManager = SettingManager.getInstance();

    protected final float gap;

    protected final ISettingModule module;
    protected final S widgetSetting;

    public SettingWidgetBase(float x, float y, float width, float height, float gap, ISettingModule module, S widgetSetting) {
        super(x, y, width, height);

        this.gap = gap;
        this.module = module;
        this.widgetSetting = widgetSetting;

        STextStyle titleStyle = STextStyle.of(colorManager.get("setting.generic.title"), 18);
        SText titleText = SText.of(titleStyle).append(widgetSetting.getName());

        if (widgetSetting.isRequired()) {
            STextStyle requiredStyle = STextStyle.of(colorManager.get("setting.generic.title.required"), 16).setBold();
            titleText.append("*", requiredStyle);
        }

        SCLabel title = new SCLabel(this, x, y, gap, height, titleText);
        title.alignment().set(Alignment.MIDDLE_LEFT);
        this.addChild(title);

        this.setDisabled(!widgetSetting.isEditable());
    }

    public void save() {
        File moduleFile = ConfigUtils.getModuleSettingsFile(this.module, true);
        if (moduleFile == null) return;

        this.settingManager.queueSave(moduleFile, this.widgetSetting.getSetting());
    }
}
