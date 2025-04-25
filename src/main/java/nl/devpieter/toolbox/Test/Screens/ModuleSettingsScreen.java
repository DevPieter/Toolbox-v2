package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.minecraft.screen.SquidScreen;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.toolbox.module.interfaces.ISettingModule;
import nl.devpieter.toolbox.widgets.ContentWidget;
import nl.devpieter.toolbox.widgets.settingWidgets.SettingGroupContainerWidget;
import nl.devpieter.toolbox.widgets.SidebarWidget;

public class ModuleSettingsScreen extends SquidScreen {

    private final ISettingModule module;

    public ModuleSettingsScreen(ISettingModule module /* TODO - Replace with class */) {
        super(Text.of("Module Settings"));
        this.module = module;
    }

    @Override
    protected void init() {
        super.init();

        ContentWidget content = new ContentWidget(424 + 16, 16, this.windowWidth - 424 - 16 - 16, this.windowHeight - 32);

        SettingGroupContainerWidget settingGroupContainer = new SettingGroupContainerWidget(content.getX(), content.getY(), content.getWidth(), content.getHeight(), 16);
        settingGroupContainer.init(this.module);

        settingGroupContainer.setParent(content);
        content.addChild(settingGroupContainer);

        this.addRenderable(content);

        SidebarWidget sidebar = new SidebarWidget(0, 16, 424, this.windowHeight - 32);

        SText text = SText.of().append(this.module.getName());
        SCLabel label = new SCLabel(sidebar, 0, 324 + 8 + 16, 424, 20, text);
        label.alignment().set(Alignment.MIDDLE_CENTER);
        sidebar.addChild(label);

        this.addRenderable(sidebar);
    }
}
