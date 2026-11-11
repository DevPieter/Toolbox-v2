package nl.devpieter.toolbox.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class SoundUtils {

    public static void playOnMaster(@NotNull SoundEvent soundEvent) {
        playOnMaster(soundEvent, 1.0F, 1.0F);
    }

    public static void playOnMaster(@NotNull SoundEvent soundEvent, float pitch) {
        playOnMaster(soundEvent, pitch, 1.0F);
    }

    public static void playOnMaster(@NotNull SoundEvent soundEvent, float pitch, float volume) {
        play(SimpleSoundInstance.forUI(soundEvent, pitch, volume));
    }

    public static void play(@NotNull SoundInstance soundInstance) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (soundManager != null) soundManager.play(soundInstance);
    }
}
