package nl.devpieter.toolbox.utils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {

    private static final Gson GSON = new Gson();

    public static @NotNull ImmutableMap<String, String> readInternalJson(String path) throws IOException {
        try (InputStream inputStream = ResourceUtils.class.getResourceAsStream(path)) {
            if (inputStream == null) throw new IOException("Resource not found: " + path);

            return readJson(inputStream);
        }
    }

    public static @NotNull ImmutableMap<String, String> readJson(InputStream inputStream) throws IOException {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

            jsonObject.entrySet().forEach(entry -> {
                String string = GsonHelper.convertToString(entry.getValue(), entry.getKey());
                builder.put(entry.getKey(), string);
            });
        }

        return builder.build();
    }
}
