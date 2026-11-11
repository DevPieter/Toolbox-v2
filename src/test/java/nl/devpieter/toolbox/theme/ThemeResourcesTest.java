package nl.devpieter.toolbox.theme;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import nl.devpieter.squid.Squid;
import nl.devpieter.squid.rendering.font.Font;
import nl.devpieter.squid.rendering.svg.SvgHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ThemeResourcesTest {

    @AfterEach
    void releaseThemeResources() {
        FontManager.getInstance().close();
        SvgManager.getInstance().close();
        ColorManager.getInstance().close();
    }

    @Test
    void fontReloadKeepsExistingStylesAndDefaultFontIdentity() throws IOException {
        TestResources firstPack = new TestResources()
                .addBundled("toolbox:font/default/regular.ttf")
                .addBundled("toolbox:font/default/bold.ttf")
                .addBundled("toolbox:font/kaushan_script/regular.ttf");
        Font defaultFont = Squid.DEFAULT_FONT;
        FontManager fonts = FontManager.getInstance();
        fonts.onResourceManagerReload(firstPack);
        Font script = fonts.get("kaushan_script");
        assertSame(defaultFont, fonts.get("default"));
        assertTrue(defaultFont.regular().isLoaded());
        assertTrue(defaultFont.hasBold());
        assertTrue(script.regular().isLoaded());
        assertNotSame(defaultFont, script);
        assertEquals(firstPack.opened, firstPack.closed);

        TestResources overridePack = new TestResources()
                .add("toolbox:font/default/regular.ttf", bundled("toolbox:font/kaushan_script/regular.ttf"))
                .addBundled("toolbox:font/kaushan_script/regular.ttf")
                .addBundled("toolbox:font/kaushan_script/italic.ttf");
        fonts.reload(overridePack);
        assertSame(defaultFont, fonts.get("default"));
        assertSame(script, fonts.get("kaushan_script"));
        assertTrue(defaultFont.regular().getTypeface().getFamilyName().contains("Kaushan"));
        assertFalse(defaultFont.hasBold(), "A removed variant must no longer retain its old native face");
        assertTrue(script.hasItalic());
        assertEquals(overridePack.opened, overridePack.closed);

        fonts.close();
        assertFalse(defaultFont.regular().isLoaded());
        assertFalse(script.regular().isLoaded());
        assertFalse(script.italic().isLoaded());
    }

    @Test
    void incompleteFontFamiliesFallBackAndRemovedFamiliesReleaseTheirFaces() throws IOException {
        FontManager fonts = FontManager.getInstance();
        fonts.reload(new TestResources().addBundled("toolbox:font/kaushan_script/regular.ttf"));
        Font script = fonts.get("kaushan_script");
        assertTrue(script.regular().isLoaded());

        TestResources incomplete = new TestResources().addBundled("toolbox:font/kaushan_script/bold.ttf");
        fonts.reload(incomplete);
        assertSame(Squid.DEFAULT_FONT, fonts.get("kaushan_script"));
        assertFalse(script.regular().isLoaded());
        assertFalse(script.bold().isLoaded());
        assertNotNull(Squid.DEFAULT_FONT.regular().getTypeface());
        assertEquals(incomplete.opened, incomplete.closed);

        fonts.reload(new TestResources().addBundled("toolbox:font/kaushan_script/regular.ttf"));
        assertSame(script, fonts.get("kaushan_script"), "Reappearing resources must update existing styles");
    }

    @Test
    void svgReloadUpdatesExistingIconAndReleasesRemovedResources() {
        SvgManager icons = SvgManager.getInstance();
        TestResources original = new TestResources().add("toolbox:svg/icons/favorite.svg", svg(20, 10));
        icons.reload(original);
        SvgHolder favorite = icons.get("icons.favorite");
        assertNotNull(favorite);
        assertTrue(favorite.isLoaded());
        assertEquals(20, favorite.getWidth());
        assertEquals(original.opened, original.closed);

        TestResources replacement = new TestResources().add("toolbox:svg/icons/favorite.svg", svg(40, 30));
        icons.onResourceManagerReload(replacement);
        assertSame(favorite, icons.get("icons.favorite"));
        assertEquals(40, favorite.getWidth());
        assertEquals(30, favorite.getHeight());
        assertEquals(replacement.opened, replacement.closed);

        icons.reload(new TestResources());
        assertNull(icons.get("icons.favorite"));
        assertFalse(favorite.isLoaded());
        icons.reload(original);
        assertSame(favorite, icons.get("icons.favorite"));
        icons.close();
        assertFalse(favorite.isLoaded());
    }

    @Test
    void colorResourcePacksOverrideKeysInPriorityOrderAndKeepArgb() {
        TestResources packs = new TestResources()
                .addText("toolbox:theme/colors.json", "{\"panel\":\"#112233\",\"faded\":\"#80112233\",\"base\":\"0x334455\"}")
                .addText("toolbox:theme/colors.json", "{\"panel\":\"#abcdef\"}")
                .addText("other:theme/colors.json", "{\"panel\":\"#ffffff\"}");
        ColorManager colors = ColorManager.getInstance();
        colors.onResourceManagerReload(packs);
        assertEquals(0xffabcdef, colors.getRGB("panel"));
        assertEquals(0x80112233, colors.getRGB("faded"));
        assertEquals(new Color(0x334455), colors.get("base"));
        assertEquals(0xffffffff, colors.getRGB("missing"));
        assertEquals(packs.opened, packs.closed);

        colors.reload(new TestResources());
        assertEquals(Color.RED, colors.get("panel", Color.RED));
    }

    private static byte[] svg(int width, int height) {
        return ("<svg xmlns='http://www.w3.org/2000/svg' width='" + width + "' height='" + height
                + "'><rect width='100%' height='100%' fill='white'/></svg>").getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] bundled(String id) throws IOException {
        Identifier path = Identifier.parse(id);
        try (InputStream input = ThemeResourcesTest.class.getResourceAsStream("/assets/" + path.getNamespace() + "/" + path.getPath())) {
            assertNotNull(input, "Missing bundled test asset " + id);
            return input.readAllBytes();
        }
    }

    private static final class TestResources implements ResourceManager {
        private final Map<Identifier, List<Resource>> resources = new LinkedHashMap<>();
        private int opened;
        private int closed;

        private TestResources addBundled(String id) throws IOException {
            return this.add(id, bundled(id));
        }

        private TestResources addText(String id, String text) {
            return this.add(id, text.getBytes(StandardCharsets.UTF_8));
        }

        private TestResources add(String id, byte[] data) {
            Resource resource = new Resource(null, () -> {
                this.opened++;
                return new ByteArrayInputStream(data) {
                    private boolean wasClosed;

                    @Override
                    public void close() throws IOException {
                        if (!this.wasClosed) {
                            this.wasClosed = true;
                            TestResources.this.closed++;
                        }
                        super.close();
                    }
                };
            });
            this.resources.computeIfAbsent(Identifier.parse(id), ignored -> new ArrayList<>()).add(resource);
            return this;
        }

        @Override
        public Set<String> getNamespaces() {
            return this.resources.keySet().stream().map(Identifier::getNamespace).collect(Collectors.toSet());
        }

        @Override
        public List<Resource> getResourceStack(Identifier id) {
            return this.resources.getOrDefault(id, List.of());
        }

        @Override
        public Optional<Resource> getResource(Identifier id) {
            List<Resource> stack = this.getResourceStack(id);
            return stack.isEmpty() ? Optional.empty() : Optional.of(stack.getLast());
        }

        @Override
        public Map<Identifier, Resource> listResources(String prefix, Predicate<Identifier> filter) {
            Map<Identifier, Resource> result = new HashMap<>();
            this.listResourceStacks(prefix, filter).forEach((id, stack) -> result.put(id, stack.getLast()));
            return result;
        }

        @Override
        public Map<Identifier, List<Resource>> listResourceStacks(String prefix, Predicate<Identifier> filter) {
            Map<Identifier, List<Resource>> result = new LinkedHashMap<>();
            this.resources.forEach((id, stack) -> {
                if (id.getPath().startsWith(prefix + "/") && filter.test(id)) result.put(id, stack);
            });
            return result;
        }

        @Override
        public Stream<PackResources> listPacks() {
            return Stream.empty();
        }
    }
}
