package nl.devpieter.toolbox.Test.States;

public class TestState {

    public String message;
    public int count;
    public boolean enabled;

    public TestState(String message, boolean enabled) {
        this.message = message;
        this.count = 0;
        this.enabled = enabled;
    }

    public String message() {
        return message;
    }

    public void message(String message) {
        this.message = message;
    }

    public int count() {
        return count;
    }

    public void count(int count) {
        this.count = count;
    }

    public boolean enabled() {
        return enabled;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }
}
