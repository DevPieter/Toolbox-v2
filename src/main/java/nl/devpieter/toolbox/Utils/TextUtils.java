package nl.devpieter.toolbox.Utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextUtils {

    public static void drawText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull Text text, float x, float y, float scale, boolean shadow) {
        drawText(matrices, textRenderer, text.asOrderedText(), x, y, scale, shadow);
    }

    public static void drawText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull OrderedText text, float x, float y, float scale, boolean shadow) {
        matrices.push();
        matrices.scale(scale, scale, 1);
        matrices.translate(x / scale, y / scale, 0);

        if (shadow) textRenderer.drawWithShadow(matrices, text, 0, 0, 0xFFFFFF);
        else textRenderer.draw(matrices, text, 0, 0, 0xFFFFFF);

        matrices.pop();
    }

    public static void drawCenteredText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull Text text, float x, float y, int width, float scale, boolean shadow) {
        drawCenteredText(matrices, textRenderer, text.asOrderedText(), x, y, width, scale, shadow);
    }

    public static void drawCenteredText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull OrderedText text, float x, float y, int width, float scale, boolean shadow) {
        int textWidth = textRenderer.getWidth(text);
        float textX = x + (width / 2F) - (textWidth * scale / 2F);

        drawText(matrices, textRenderer, text, textX, y, scale, shadow);
    }

    public static void drawTruncatedText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull Text text, Style dotStyle, float x, float y, int width, float scale, boolean shadow) {
        float maxWidth = (width - 4) / scale; // 4 = some extra space for the dots <3
        List<OrderedText> lines = textRenderer.wrapLines(text, MathHelper.floor(maxWidth));

        OrderedText finalText = lines.size() > 1 ? OrderedText.concat(lines.get(0), withStyle("...", dotStyle).asOrderedText()) : lines.get(0);
        drawText(matrices, textRenderer, finalText, x, y, scale, shadow);
    }

    public static void drawTruncatedMultilineText(@NotNull MatrixStack matrices, @NotNull TextRenderer textRenderer, @NotNull Text text, Style dotStyle, float x, float y, int width, int height, float scale, boolean shadow) {
        float maxWidth = (width - 4) / scale; // 4 = some extra space for the dots <3
        float maxHeight = height / scale;
        float lineHeight = textRenderer.fontHeight * 1.1F;

        List<OrderedText> lines = textRenderer.wrapLines(text, MathHelper.floor(maxWidth));
        int maxLines = MathHelper.clamp(MathHelper.floor(maxHeight / lineHeight), 1, lines.size());

        for (int i = 0; i < maxLines; i++) {
            if (i == maxLines - 1 && maxLines != lines.size()) {
                OrderedText finalText = OrderedText.concat(lines.get(i), withStyle("...", dotStyle).asOrderedText());
                drawText(matrices, textRenderer, finalText, x, y + (i * lineHeight), scale, shadow);
                break;
            }

            drawText(matrices, textRenderer, lines.get(i), x, y + (i * lineHeight), scale, shadow);
        }
    }

    public static MutableText currentOrDefaultStyle(@NotNull Text text, @NotNull Style defaultStyle) {
        return Text.of("").copy().setStyle(defaultStyle).append(text);
    }

    public static MutableText withStyle(@NotNull String text, @NotNull Style style) {
        return withStyle(Text.of(text), style);
    }

    public static MutableText withStyle(@NotNull Text text, @NotNull Style style) {
        return text.copy().setStyle(style);
    }

    public static MutableText clearStyle(@NotNull String text) {
        return clearStyle(Text.of(text));
    }

    public static MutableText clearStyle(@NotNull Text text) {
        return text.copy().setStyle(Style.EMPTY);
    }
}
