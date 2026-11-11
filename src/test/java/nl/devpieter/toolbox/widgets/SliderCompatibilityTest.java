package nl.devpieter.toolbox.widgets;

import io.github.humbleui.skija.Surface;
import nl.devpieter.squid.components.SCNumberInputDisplay;
import nl.devpieter.squid.components.SCProgressBar;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.rendering.SkiaContext;
import nl.devpieter.toolbox.module.interfaces.ISettingModule;
import nl.devpieter.toolbox.setting.interfaces.IClampedSetting;
import nl.devpieter.toolbox.setting.settings.ClampedFloatSetting;
import nl.devpieter.toolbox.setting.settings.ClampedIntSetting;
import nl.devpieter.toolbox.setting.widgetSettings.NumberWidgetSetting;
import nl.devpieter.toolbox.widgets.settingWidgets.SliderSettingWidget;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SliderCompatibilityTest {

    @Test
    void integerSettingUsesNormalizedProgressAndDisplaysItsNumericValue() {
        ClampedIntSetting setting = new ClampedIntSetting("integer", 65, 0, 100);
        TestSlider<Integer> slider = new TestSlider<>(setting);
        SCProgressBar bar = bar(slider);

        assertEquals(65, setting.getValue());
        assertEquals(0, slider.saveCount, "opening settings does not enqueue a save");
        assertEquals(0.65f, bar.getValue(), 0.00001f);
        assertEquals(bar.getX() + bar.getWidth() * 0.65f, bar.getProgressX(), 0.001f);

        render(slider);
        SCNumberInputDisplay display = slider.getChildren().stream()
                .filter(SCNumberInputDisplay.class::isInstance)
                .map(SCNumberInputDisplay.class::cast)
                .findFirst().orElseThrow();
        assertEquals(65, display.getValue(), 0.001);
    }

    @Test
    void draggingTheSliderReachesBothSettingBounds() {
        ClampedIntSetting setting = new ClampedIntSetting("drag", 65, 0, 100);
        TestSlider<Integer> slider = new TestSlider<>(setting);
        SCProgressBar bar = bar(slider);
        float middleX = bar.getX() + bar.getWidth() / 2;
        float y = bar.getY() + bar.getHeight() / 2;

        assertTrue(slider.mouseClicked(middleX, y, 0));
        assertTrue(slider.mouseDragged(bar.getX() + bar.getWidth() + 20, y, 0, 0, 0));
        assertEquals(100, setting.getValue());
        assertEquals(1, bar.getValue(), 0.00001f);

        assertTrue(slider.mouseDragged(bar.getX() - 20, y, 0, 0, 0));
        assertEquals(0, setting.getValue());
        assertEquals(0, bar.getValue(), 0.00001f);
    }

    @Test
    void wheelChangesIntegerAndFractionalSettingsByTenPercent() {
        ClampedIntSetting integer = new ClampedIntSetting("integer-wheel", 65, 0, 100);
        TestSlider<Integer> integerSlider = new TestSlider<>(integer);
        scroll(integerSlider, 1);
        assertEquals(75, integer.getValue());
        scroll(integerSlider, -1);
        assertEquals(65, integer.getValue());

        ClampedFloatSetting fractional = new ClampedFloatSetting("fractional-wheel", 0.65f, 0, 1);
        TestSlider<Float> fractionalSlider = new TestSlider<>(fractional);
        assertEquals(0.65f, fractional.getValue(), 0.00001f, "opening the widget preserves fractional values");
        assertEquals(0, fractionalSlider.saveCount);
        scroll(fractionalSlider, 1);
        assertEquals(0.75f, fractional.getValue(), 0.00001f);
        scroll(fractionalSlider, -1);
        assertEquals(0.65f, fractional.getValue(), 0.00001f);
    }

    @Test
    void fixedBoundsProduceFiniteProgressAndKeepTheSettingValue() {
        ClampedIntSetting setting = new ClampedIntSetting("fixed", 65, 65, 65);
        TestSlider<Integer> slider = new TestSlider<>(setting);
        SCProgressBar bar = bar(slider);

        assertTrue(Float.isFinite(bar.getValue()));
        assertTrue(Float.isFinite(bar.getProgressX()));
        assertEquals(0, bar.getValue(), 0.00001f);
        assertEquals(65, setting.getValue());
        scroll(slider, 1);
        assertEquals(65, setting.getValue());
        render(slider);
    }

    private static void scroll(TestSlider<?> slider, double amount) {
        SCProgressBar bar = bar(slider);
        float x = bar.getX() + bar.getWidth() / 2;
        float y = bar.getY() + bar.getHeight() / 2;
        slider.mouseMoved(x, y);
        assertTrue(slider.mouseScrolled(x, y, 0, amount));
    }

    private static SCProgressBar bar(TestSlider<?> slider) {
        return slider.getChildren().stream().filter(SCProgressBar.class::isInstance)
                .map(SCProgressBar.class::cast).findFirst().orElseThrow();
    }

    private static void render(IRenderable widget) {
        try (Surface surface = Surface.makeRasterN32Premul(700, 60);
             SkiaContext context = new SkiaContext(surface.getCanvas())) {
            widget.render(context, 0, 0, 0);
        }
    }

    private static final class TestSlider<T extends Number> extends SliderSettingWidget<T, NumberWidgetSetting<T>> {
        private int saveCount;

        private TestSlider(IClampedSetting<T> setting) {
            super(20, 10, 600, 250, new ISettingModule() {
                @Override
                public String getName() {
                    return "Test";
                }

                @Override
                public String getDescription() {
                    return "Slider regression fixture";
                }
            }, new NumberWidgetSetting<>("Value", "", NumberWidgetSetting.WidgetType.SLIDER, setting));
        }

        @Override
        public void save() {
            // Exercise real widget events without creating configuration files.
            this.saveCount++;
        }
    }
}
