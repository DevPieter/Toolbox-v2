package nl.devpieter.toolbox.Theme;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class CardManager extends DrawableHelper {

    private static final CardManager INSTANCE = new CardManager();

    public static CardManager getInstance() {
        return INSTANCE;
    }

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final TextureManager textureManager = client.getTextureManager();

    private final HashMap<Integer, Identifier> cardTextureCache = new HashMap<>();
    private final HashMap<Integer, Identifier> cardShadowTextureCache = new HashMap<>();

    private CardManager() {
    }

    public void drawCardWithShadow(@NotNull MatrixStack matrices, int x, int y, int width, int height, int leftRadius, int rightRadius, int shadowRadius, float upScaleFactor, float @NotNull [] cardColor, float @NotNull [] shadowColor) {
        width = (int) (width * upScaleFactor);
        height = (int) (height * upScaleFactor);

        leftRadius = (int) (leftRadius * upScaleFactor);
        rightRadius = (int) (rightRadius * upScaleFactor);

        shadowRadius = (int) (shadowRadius * upScaleFactor);

        Identifier cardIdentifier = this.getCardTexture(width, height, leftRadius, rightRadius);
        Identifier shadowIdentifier = this.getCardShadowTexture(width, height, leftRadius + 5, rightRadius + 5, shadowRadius);

        int[] shadowSize = this.calculateShadowSize(width, height, shadowRadius);

        matrices.push();
        matrices.scale(1.0F / upScaleFactor, 1.0F / upScaleFactor, 1.0F);
        matrices.translate(x, y, 0);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(shadowColor[0], shadowColor[1], shadowColor[2], shadowColor[3]);
        RenderSystem.setShaderTexture(0, shadowIdentifier);
        drawTexture(matrices, -5, -5, 0, 0, shadowSize[0], shadowSize[1], shadowSize[0], shadowSize[1]);

        RenderSystem.setShaderColor(cardColor[0], cardColor[1], cardColor[2], cardColor[3]);
        RenderSystem.setShaderTexture(0, cardIdentifier);
        drawTexture(matrices, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        matrices.pop();
    }

    public @Nullable Identifier getCardTexture(int width, int height, int leftRadius, int rightRadius) {
        int hash = this.calculateHash(width, height, leftRadius, rightRadius, 1);
        if (this.cardTextureCache.containsKey(hash)) return this.cardTextureCache.get(hash);

        BufferedImage cardImage = this.createRoundedRect(width, height, leftRadius, rightRadius);
        Identifier cardIdentifier = new Identifier("toolbox", String.format("generated/card/%s.png", hash));

        if (this.registerTexture(cardIdentifier, cardImage)) {
            this.cardTextureCache.put(hash, cardIdentifier);
        }

        return cardIdentifier;
    }

    public @Nullable Identifier getCardShadowTexture(int width, int height, int leftRadius, int rightRadius, int shadowSize) {
        int hash = this.calculateHash(width, height, leftRadius, rightRadius, shadowSize);
        if (this.cardShadowTextureCache.containsKey(hash)) return this.cardShadowTextureCache.get(hash);

        BufferedImage cardImage = this.createRoundedRect(width, height, leftRadius, rightRadius);
        BufferedImage shadowImage = this.createDropShadow(cardImage, shadowSize);

        Identifier shadowIdentifier = new Identifier("toolbox", String.format("generated/card_shadow/%s.png", hash));

        if (this.registerTexture(shadowIdentifier, shadowImage)) {
            this.cardShadowTextureCache.put(hash, shadowIdentifier);
        }

        return shadowIdentifier;
    }

    private boolean registerTexture(Identifier identifier, BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            byteBuffer.flip();

            NativeImageBackedTexture texture = new NativeImageBackedTexture(NativeImage.read(byteBuffer));
            this.textureManager.registerTexture(identifier, texture);

            return true;
        } catch (Exception e) {
            Toolbox.LOGGER.error("Failed to register texture {} while generating card texture", identifier, e);
            return false;
        }
    }

    private @NotNull BufferedImage createRoundedRect(int width, int height, int leftRadius, int rightRadius) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fill(new LeftRadius(0, 0, (float) width / 2, height, leftRadius));
        graphics.fill(new RightRadius((float) width / 2, 0, (float) width / 2, height, rightRadius));
        graphics.dispose();

        return image;
    }

    private @NotNull BufferedImage createDropShadow(@NotNull BufferedImage image, int size) {
        int width = image.getWidth() + size * 2;
        int height = image.getHeight() + size * 2;

        BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = mask.createGraphics();
        graphics.drawImage(image, size, size, null);
        graphics.dispose();

        mask = createBlurOp(size).filter(mask, null);

        Graphics2D finalGraphics = mask.createGraphics();
        finalGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1));
        finalGraphics.setColor(Color.WHITE);
        finalGraphics.fillRect(0, 0, width, height);
        finalGraphics.dispose();

        return mask;
    }

    private @NotNull ConvolveOp createBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1f / (float) (size * size);
        Arrays.fill(data, value);
        return new ConvolveOp(new Kernel(size, size, data), ConvolveOp.EDGE_ZERO_FILL, null);
    }

    private int calculateHash(int width, int height, int leftRadius, int rightRadius, int extra) {
        return (width << 24) | (height << 16) | (leftRadius << 8) | (rightRadius << 4) | extra;
    }

    private int @NotNull [] calculateShadowSize(int width, int height, int size) {
        int[] shadowSize = new int[2];
        shadowSize[0] = width + size * 2;
        shadowSize[1] = height + size * 2;
        return shadowSize;
    }

    private static class LeftRadius extends Path2D.Float {
        public LeftRadius(float x, float y, float width, float height, float radius) {
            this.moveTo(x + radius, y);
            this.lineTo(x + width, y);
            this.lineTo(x + width, y + height);
            this.lineTo(x + radius, y + height);
            this.quadTo(x, y + height, x, y + height - radius);
            this.lineTo(x, y + radius);
            this.quadTo(x, y, x + radius, y);
            this.closePath();
        }
    }

    private static class RightRadius extends Path2D.Float {
        public RightRadius(float x, float y, float width, float height, float radius) {
            this.moveTo(x, y);
            this.lineTo(x + width - radius, y);
            this.quadTo(x + width, y, x + width, y + radius);
            this.lineTo(x + width, y + height - radius);
            this.quadTo(x + width, y + height, x + width - radius, y + height);
            this.lineTo(x, y + height);
            this.closePath();
        }
    }
}
