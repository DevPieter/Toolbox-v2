package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.layout.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.screen.SquidScreen;
import nl.devpieter.squid.widgets.SSlider;
import nl.devpieter.squid.widgets.STextField;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.ContentWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;
import nl.devpieter.toolbox.setting.ISetting;
import nl.devpieter.toolbox.setting.settings.IntSetting;
import nl.devpieter.toolbox.setting.settings.StringSetting;

public class ModuleSettingsScreen extends SquidScreen {

    private final ISettingModule module;

    public ModuleSettingsScreen(ISettingModule module) {
        super(Text.of("Module Settings"));
        this.module = module;
    }

    @Override
    protected void init() {
        super.init();

        ContentWidget content = new ContentWidget(424 + 16, 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 32);
        this.addWidget(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);

        SText text = SText.of().append(this.module.getName().getString());
        SCLabel label = new SCLabel(sidebar, 0, 324 + 8 + 16, 424, 20, text);
        label.setAlignment(Alignment.MIDDLE_CENTER);
        sidebar.addChild(label);

        this.addWidget(sidebar);

        int y = 100;

        for (int i = 0; i < this.module.getSettings().size(); i++) {
            ISetting<?> setting = this.module.getSettings().get(i);

//            SText settingText = SText.of().append(setting.getIdentifier());
//            SCLabel settingLabel = new SCLabel(content, 500, y, 100, 20, settingText);
//            content.addChild(settingLabel);

            if (setting instanceof IntSetting intSetting) {
                SText valueText = SText.of().append(setting.getIdentifier());
                SSlider<Integer> slider = new SSlider<>(valueText, 0, 100, 500, y, 500, 120);
                slider.setValue(intSetting.getValue());

                content.addChild(slider);
            }
            if (setting instanceof StringSetting stringSetting) {
                STextField textField = new STextField(500, y, 500 + 120, 30, setting.getIdentifier());
                content.addChild(textField);
            }

            y += 50;
        }
    }
}
