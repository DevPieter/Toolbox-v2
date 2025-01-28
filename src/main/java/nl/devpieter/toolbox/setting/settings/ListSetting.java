package nl.devpieter.toolbox.setting.settings;

import com.google.common.reflect.TypeToken;
import nl.devpieter.toolbox.setting.SettingBase;

import java.lang.reflect.Type;
import java.util.List;

public class ListSetting<T> extends SettingBase<List<T>> {

    public ListSetting(String identifier, List<T> defaultValue) {
        super(identifier, defaultValue);
    }

    public ListSetting(String identifier, List<T> defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public Type getType() {
        return new TypeToken<List<T>>() {
        }.getType();
    }

    public void add(T value) {
        getValue().add(value);
    }

    public void remove(T value) {
        getValue().remove(value);
    }

    public void removeFirst() {
        getValue().removeFirst();
    }

    public void removeLast() {
        getValue().removeLast();
    }

    public void clear() {
        getValue().clear();
    }
}
