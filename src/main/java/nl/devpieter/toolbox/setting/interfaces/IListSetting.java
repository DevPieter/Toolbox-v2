package nl.devpieter.toolbox.setting.interfaces;

import java.util.List;

public interface IListSetting<T> extends ISetting<List<T>> {

    void add(T value);

    void remove(T value);

    void removeAt(int index);

    void removeFirst();

    void removeLast();

    void clear();
}
