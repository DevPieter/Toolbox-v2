package nl.devpieter.toolbox.Module.Interfaces;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import nl.devpieter.toolbox.Utils.SoundUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public interface IInteractableModule extends IModule {

    enum Action {
        CONTINUE,
        CANCEL,
        CONTINUE_NO_SOUND,
        CANCEL_NO_SOUND
    }

    /**
     * TODO - Add (better) documentation (not only for this method)
     * (-1 if no button was pressed)
     */
    default @Nullable SoundEvent getInteractSound(int button) {
        return SoundEvents.ENTITY_ITEM_PICKUP;
    }

    /**
     * (-1 if no button was pressed)
     */

    default float getInteractSoundPitch(int button) {
        return button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? 1.5F : 1.0F;
    }

    /**
     * (-1 if no button was pressed)
     */
    static void playSound(IInteractableModule module, Action action, int button) {
        if (action == null || action.name().endsWith("NO_SOUND")) return;

        SoundEvent soundEvent = module.getInteractSound(button);
        if (soundEvent != null) SoundUtils.playOnMaster(soundEvent, module.getInteractSoundPitch(button));
    }
}
