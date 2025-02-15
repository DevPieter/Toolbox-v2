package nl.devpieter.toolbox.Widgets.SettingWidgets;

import net.minecraft.util.math.MathHelper;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.renderable.component.ComponentBase;
import nl.devpieter.toolbox.Module.Interfaces.ISettingModule;
import nl.devpieter.toolbox.setting.abstractions.ISettingGroup;

import java.util.List;

public class SettingGroupContainerWidget extends ComponentBase {

    private final float gap;

    private final SCContainer container;

    public SettingGroupContainerWidget(float x, float y, float width, float height, float gap) {
        super(x, y, width, height);

        this.gap = gap;

        this.container = new SCContainer(this, x, y, width, height);
        this.container.setParent(this);
        this.addChild(this.container);
    }

    public void init(ISettingModule module) {
        this.container.clearChildren();

        List<ISettingGroup> settingGroups = module.getSettingGroups();
        if (settingGroups.isEmpty()) return;

        float minWidth = 400;
        float maxWidth = 600;

        int maxPerRow = this.fittingGroupsRow(this.getWidth(), minWidth, maxWidth, this.gap);
        if (maxPerRow <= 0) return;

        float groupWidth = this.calculateGroupWidth(this.getWidth(), this.gap, maxPerRow);
        if (groupWidth < minWidth) return;

        float[] heights = new float[maxPerRow];

        for (int i = 0; i < settingGroups.size(); i++) {
            int row = i % maxPerRow;

            float x = (row) * (groupWidth + this.gap) + this.getX() + this.gap;
            float y = heights[row] + this.getY() + this.gap;

            SettingGroupWidget settingGroupWidget = new SettingGroupWidget(x, y, groupWidth, this.gap, module, settingGroups.get(i));
            settingGroupWidget.setParent(this.container);
            this.container.addChild(settingGroupWidget);

            heights[row] += settingGroupWidget.getHeight() + this.gap;
        }
    }

    @Override
    protected void render(long context, float delta) {

    }

    private int fittingGroupsRow(float width, float minWidth, float maxWidth, float gap) {
        return MathHelper.clamp((int) ((width + gap) / (minWidth + gap)), 1, (int) ((width + gap) / (maxWidth + gap)));
    }

    private float calculateGroupWidth(float width, float gap, int amount) {
        return (width - gap * (amount + 1)) / amount;
    }
}
