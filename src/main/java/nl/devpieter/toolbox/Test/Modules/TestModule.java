package nl.devpieter.toolbox.Test.Modules;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Module.Interfaces.IClickableModule;
import nl.devpieter.toolbox.Module.Interfaces.IInteractableModule;
import nl.devpieter.toolbox.Module.Interfaces.IStatefulModule;
import nl.devpieter.toolbox.Module.Interfaces.IToggleableModule;
import nl.devpieter.toolbox.Module.ModuleManager;
import nl.devpieter.toolbox.Test.States.TestState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestModule implements IStatefulModule<TestState>, IToggleableModule, IClickableModule {

    private final TestState defaultState = new TestState("Hello World!, I'm a test module!", false);
    private TestState state = this.defaultState;

    @Override
    public @NotNull Text getName() {
//        return Text.of("Test Module with a very 1 2 3 long name that is way too long for a module name");
//        return TextHelper.withStyle("Test Module with a very 1 2 3 long name that is way too long for a module name", Style.EMPTY.withColor(0x00FF00));
        return Text.of("Test Module");
    }

    @Override
    public @NotNull Text getDescription() {
        return Text.of("Lorem Ipsum is simply dummy text of 1 2 the printing and typesetting industry. This is a test module. with a1 2");
    }

    @Nullable
    @Override
    public TestState getState() {
        return this.state;
    }

    @Override
    public void setState(@Nullable TestState state) {
        if (state == null) {
            this.resetState();
            return;
        }

        System.out.println("Setting state to " + state.message());
        this.state = state;
    }

    @Override
    public void resetState() {
        System.out.println("Resetting state...");
        this.state = this.defaultState;
    }

    @Override
    public boolean isEnabled() {
        return this.state.enabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        System.out.println("Setting enabled to " + enabled);
        this.state.enabled(enabled);
        ModuleManager.getInstance().saveState(this.getClass());
    }

    @Override
    public void onRegistered() {
        System.out.println("Registered!");

        this.state.count(this.state.count() + 1);
        ModuleManager.getInstance().saveState(this.getClass());
    }

    @Override
    public void onUnregistered() {
        System.out.println("Unregistered!");
    }

    @Override
    public IInteractableModule.Action onEnable() {
        System.out.println("Enabling...");
        return Action.CONTINUE_NO_SOUND;
    }

    @Override
    public void onEnabled() {
        System.out.println("Enabled!");
    }

    @Override
    public IInteractableModule.Action onDisable() {
        System.out.println("Disabling...");
        return Action.CONTINUE_NO_SOUND;
    }

    @Override
    public void onDisabled() {
        System.out.println("Disabled!");
    }

    @Override
    public void onClick(int button) {
        System.out.println("Clicked! Button: " + button);
    }

    @Override
    public @NotNull IInteractableModule.Action onLeftClick() {
        System.out.println("Left Clicked!");
        return Action.CONTINUE;
    }

    @Override
    public @NotNull IInteractableModule.Action onMiddleClick() {
        System.out.println("Middle Clicked!");
        return Action.CANCEL;
    }

    @Override
    public @NotNull IInteractableModule.Action onRightClick() {
        System.out.println("Right Clicked!");
        return Action.CONTINUE_NO_SOUND;
    }
}