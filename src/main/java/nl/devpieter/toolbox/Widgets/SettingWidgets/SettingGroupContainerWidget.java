package nl.devpieter.toolbox.Widgets.SettingWidgets;

import net.minecraft.util.math.MathHelper;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.components.SCLabel;
import nl.devpieter.squid.enums.Alignment;
import nl.devpieter.squid.rendering.text.SText;
import nl.devpieter.squid.rendering.text.STextStyle;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.Widgets.WidgetBase;
import nl.devpieter.toolbox.setting.SettingGroup;

import java.util.List;

public class SettingGroupContainerWidget extends WidgetBase {

    public static final float MIN_WIDTH = 400;
    public static final float MAX_WIDTH = 600;

    private final float gap;

    private final SCLabel message;
    private final SCContainer container;

    public SettingGroupContainerWidget(float x, float y, float width, float height, float gap) {
        super(x, y, width, height);

        this.gap = gap;

        STextStyle messageStyle = STextStyle.of(colorManager.get("setting.group.container.message"), 20).setItalic();
        this.message = new SCLabel(this, x, y + 32, width, 24, SText.of("", messageStyle));
        this.message.alignment().set(Alignment.MIDDLE_CENTER).setVisible(false);
        this.addChild(this.message);

        this.container = new SCContainer(this, x, y, width, height);
        this.addChild(this.container);
    }

    public void init(ISettingModule module /* TODO - Replace with class */) {
        this.message.setVisible(false);
        this.container.clearChildren();

        List<SettingGroup> settingGroups = module.getSettingGroups();
        if (settingGroups.isEmpty()) {
            this.message.text().get().set("There are no settings in this module.");
            this.message.setVisible(true);
            return;
        }

        int maxPerRow = this.fittingGroupsRow(this.getWidth(), this.gap);
        if (maxPerRow <= 0) {
            this.message.text().get().set("Screen too small.");
            this.message.setVisible(true);
            return;
        }

        float groupWidth = this.calculateGroupWidth(this.getWidth(), this.gap, maxPerRow);
        if (groupWidth < MIN_WIDTH) {
            this.message.text().get().set("Screen too small.");
            this.message.setVisible(true);
            return;
        }

        float[] heights = new float[maxPerRow];

        for (int i = 0; i < settingGroups.size(); i++) {
            int row = i % maxPerRow;

            float x = (row) * (groupWidth + this.gap) + this.getX() + this.gap;
            float y = heights[row] + this.getY() + this.gap;

            SettingGroupWidget settingGroupWidget = new SettingGroupWidget(x, y, groupWidth, 8, module, settingGroups.get(i));
            settingGroupWidget.setParent(this.container);
            this.container.addChild(settingGroupWidget);

            heights[row] += settingGroupWidget.getHeight() + this.gap;
        }
    }

    @Override
    protected void render(long context, float delta) {

    }

    private int fittingGroupsRow(float width, float gap) {
        return MathHelper.clamp((int) ((width + gap) / (MIN_WIDTH + gap)), 1, (int) ((width + gap) / (MAX_WIDTH + gap)));
    }

    private float calculateGroupWidth(float width, float gap, int amount) {
        return (width - gap * (amount + 1)) / amount;
    }
}
