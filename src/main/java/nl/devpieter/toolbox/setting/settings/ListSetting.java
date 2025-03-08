package nl.devpieter.toolbox.setting.settings;

import com.google.common.reflect.TypeToken;
import nl.devpieter.toolbox.setting.base.SettingBase;
import nl.devpieter.toolbox.setting.interfaces.IListSetting;

import java.lang.reflect.Type;
import java.util.List;

public class ListSetting<T> extends SettingBase<List<T>> implements IListSetting<T> {

    public ListSetting(String identifier, List<T> defaultValue) {
        super(identifier, defaultValue);
    }

    public ListSetting(String identifier, List<T> defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public void add(T value) {
        getValue().add(value);
    }

    @Override
    public void remove(T value) {
        getValue().remove(value);
    }

    @Override
    public void removeAt(int index) {
        getValue().remove(index);
    }

    @Override
    public void removeFirst() {
        getValue().removeFirst();
    }

    @Override
    public void removeLast() {
        getValue().removeLast();
    }

    @Override
    public void clear() {
        getValue().clear();
    }

    @Override
    public Type getType() {
        return new TypeToken<List<T>>() {
        }.getType();
    }
}
