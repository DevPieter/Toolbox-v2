package nl.devpieter.toolbox.widgets;

import io.github.humbleui.skija.Surface;
import nl.devpieter.squid.components.SCContainer;
import nl.devpieter.squid.renderable.IRenderable;
import nl.devpieter.squid.rendering.SkiaContext;
import nl.devpieter.toolbox.module.interfaces.IModule;
import nl.devpieter.toolbox.widgets.moduleWidgets.ModuleCarouselWidget;
import nl.devpieter.toolbox.widgets.moduleWidgets.ModuleContainerWidget;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarouselCompatibilityTest {

    @Test
    void carouselPagesRetainOneVisiblePageAndRenderWithoutOptionalIcons() {
        ModuleCarouselWidget carousel = new ModuleCarouselWidget(10, 10, 500, 220, 16);
        carousel.init(List.of(First.class, Second.class, Third.class));
        SCContainer pages = carousel.getChildren().stream().filter(SCContainer.class::isInstance)
                .map(SCContainer.class::cast).findFirst().orElseThrow();

        assertEquals(3, carousel.getPageCount());
        assertEquals(List.of(true, false, false), visibility(pages));
        carousel.navigateToNext();
        assertEquals(1, carousel.getCurrentPage());
        assertEquals(List.of(false, true, false), visibility(pages));
        carousel.navigateToLast();
        assertEquals(List.of(false, false, true), visibility(pages));

        // No resource manager or Minecraft client is required for the Skia draw path.
        try (Surface surface = Surface.makeRasterN32Premul(550, 250);
             SkiaContext context = new SkiaContext(surface.getCanvas())) {
            carousel.render(context, 0, 0, 0);
        }

        carousel.init(List.of());
        assertEquals(0, carousel.getPageCount());
        assertEquals(0, carousel.getCurrentPage());
    }

    @Test
    void gridCapacityUsesTheMinecraft262MathHelpers() {
        assertEquals(4, ModuleContainerWidget.fittingCards(900, 400, 32));
        assertEquals(0, ModuleContainerWidget.fittingCards(350, 100, 32));
    }

    private static List<Boolean> visibility(SCContainer pages) {
        return pages.getChildren().stream().map(IRenderable::isVisible).toList();
    }

    private static class First implements IModule {
        @Override
        public String getName() {
            return "First";
        }

        @Override
        public String getDescription() {
            return "Test module";
        }
    }

    private static final class Second extends First {
    }

    private static final class Third extends First {
    }
}
