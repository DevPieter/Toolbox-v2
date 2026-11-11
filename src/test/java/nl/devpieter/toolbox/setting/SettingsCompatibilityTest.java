package nl.devpieter.toolbox.setting;

import com.google.gson.JsonParser;
import nl.devpieter.toolbox.setting.interfaces.ISetting;
import nl.devpieter.toolbox.setting.settings.BooleanSetting;
import nl.devpieter.toolbox.setting.settings.IntSetting;
import nl.devpieter.toolbox.setting.settings.ListSetting;
import nl.devpieter.toolbox.setting.settings.StringSetting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SettingsCompatibilityTest {

    @TempDir
    Path directory;

    @Test
    void legacySettingsRetainValuesTypesFavoritesAndUnicode() throws Exception {
        Path file = directory.resolve("Settings.json");
        Files.writeString(file, """
                [
                  {"key":"enabled","value":true},
                  {"key":"amount","value":65},
                  {"key":"label","value":"café 🦑"},
                  {"key":"favorite_modules","value":["example.Tools","example.Visuals"]}
                ]
                """);
        BooleanSetting enabled = new BooleanSetting("enabled", false);
        IntSetting amount = new IntSetting("amount", 0);
        StringSetting label = new StringSetting("label", "default");
        ListSetting<String> favorites = new ListSetting<>("favorite_modules", new ArrayList<>());
        List<ISetting<?>> settings = List.of(enabled, amount, label, favorites);

        assertTrue(SettingManager.getInstance().loadSettings(file.toFile(), settings));
        assertTrue(enabled.getValue());
        assertEquals(65, amount.getValue());
        assertEquals("café 🦑", label.getValue());
        assertEquals(List.of("example.Tools", "example.Visuals"), favorites.getValue());

        assertTrue(SettingManager.getInstance().queueSave(file.toFile(), settings));
        SettingManager.getInstance().forceSaveQueue();
        enabled.setValue(false);
        amount.setValue(-1);
        favorites.setValue(new ArrayList<>());
        assertTrue(SettingManager.getInstance().loadSettings(file.toFile(), settings));
        assertTrue(enabled.getValue());
        assertEquals(65, amount.getValue());
        assertEquals(List.of("example.Tools", "example.Visuals"), favorites.getValue());
    }

    @Test
    void queuedChangesReplaceDuplicateKeysAndPreserveOtherSettings() throws Exception {
        Path file = directory.resolve("Settings.json");
        Files.writeString(file, """
                [{"key":"amount","value":15},{"key":"unregistered_module","value":{"enabled":true}}]
                """);
        IntSetting amount = new IntSetting("amount", 0);
        amount.setValue(42);
        SettingManager.getInstance().queueSave(file.toFile(), amount);
        amount.setValue(87);
        SettingManager.getInstance().queueSave(file.toFile(), amount);
        SettingManager.getInstance().forceSaveQueue();

        var saved = JsonParser.parseString(Files.readString(file)).getAsJsonArray();
        assertEquals(2, saved.size(), "saving should preserve settings belonging to other modules");
        long amountEntries = saved.asList().stream()
                .filter(value -> value.getAsJsonObject().get("key").getAsString().equals("amount")).count();
        assertEquals(1, amountEntries);
        assertTrue(SettingManager.getInstance().loadSetting(file.toFile(), amount));
        assertEquals(87, amount.getValue());
        assertTrue(saved.asList().stream().anyMatch(value -> {
            var entry = value.getAsJsonObject();
            return entry.get("key").getAsString().equals("unregistered_module")
                    && entry.getAsJsonObject("value").get("enabled").getAsBoolean();
        }));
    }
}
