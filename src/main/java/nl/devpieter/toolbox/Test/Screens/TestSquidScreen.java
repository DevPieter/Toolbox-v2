package nl.devpieter.toolbox.Test.Screens;

import net.minecraft.text.Text;
import nl.devpieter.squid.rendering.font.FontUtils;
import nl.devpieter.squid.screen.SquidScreen;
import nl.devpieter.toolbox.Toolbox;
import nl.devpieter.toolbox.Widgets.CategoryWidgets.CategoryCardWidget;
import nl.devpieter.toolbox.Widgets.SidebarWidget;

public class TestSquidScreen extends SquidScreen {

    public TestSquidScreen() {
        super(Text.of("test"));

        CategoryCardWidget category1  = new CategoryCardWidget(20, 324, 384, 67);
        CategoryCardWidget category2  = new CategoryCardWidget(20, 423, 384, 67);
        CategoryCardWidget category3  = new CategoryCardWidget(20, 522, 384, 67);

        SidebarWidget sidebar = new SidebarWidget(0, 0, 424, this.windowHeight);
        sidebar.addChild(category1);
        sidebar.addChild(category2);
        sidebar.addChild(category3);
        this.addWidget(sidebar);

    }

    @Override
    protected void render(long context, float mouseX, float mouseY, float delta) {
        if (!Toolbox.KAUSHAN_SCRIPT.getRegular().loaded()) { // TODO - Change this to a more reliable method
            FontUtils.loadFont(context, Toolbox.KAUSHAN_SCRIPT);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
