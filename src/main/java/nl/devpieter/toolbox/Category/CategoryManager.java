package nl.devpieter.toolbox.Category;

import net.minecraft.text.Text;
import nl.devpieter.toolbox.Toolbox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoryManager {

    private static CategoryManager INSTANCE;

    private final List<ICategory> categories = new ArrayList<>();

    private CategoryManager() {
    }

    public static CategoryManager getInstance() {
        if (INSTANCE == null) INSTANCE = new CategoryManager();
        return INSTANCE;
    }

    public void register(@NotNull Class<? extends ICategory> categoryClass) {
        if (this.isRegistered(categoryClass)) {
            Toolbox.LOGGER.warn("Category ({}) is already registered!", categoryClass.getSimpleName());
            return;
        }

        try {
            ICategory category = categoryClass.getConstructor().newInstance();
            this.categories.add(category);
        } catch (Exception e) {
            Toolbox.LOGGER.error("Category ({}) could not be registered! Make sure the category has a public no-args constructor.", categoryClass.getSimpleName(), e);
        }
    }

    public void unregister(@NotNull Class<? extends ICategory> categoryClass) {
        ICategory category = this.getInstance(categoryClass);
        if (category == null) {
            Toolbox.LOGGER.warn("Category ({}) is not registered!", categoryClass.getSimpleName());
            return;
        }

        this.categories.remove(category);
    }

    public List<Class<? extends ICategory>> getCategories() {
        return List.copyOf(this.categories.stream().map(ICategory::getClass).toList());
    }

    public List<Class<? extends ICategory>> getCategories(boolean sorted) {
        if (!sorted) return this.getCategories();

        return List.copyOf(this.categories.stream()
                .sorted(Comparator.comparingInt(ICategory::getWeight).thenComparing(category -> category.getName().getString()))
                .map(ICategory::getClass)
                .toList()
        );
    }

    public boolean isRegistered(@NotNull Class<? extends ICategory> categoryClass) {
        return this.categories.stream().anyMatch(category -> category.getClass().equals(categoryClass));
    }

    public Text getName(@NotNull Class<? extends ICategory> categoryClass) {
        ICategory category = this.getInstance(categoryClass);
        return category != null ? category.getName() : Text.of("Unknown");
    }

    public Text getDescription(@NotNull Class<? extends ICategory> categoryClass) {
        ICategory category = this.getInstance(categoryClass);
        return category != null ? category.getDescription() : Text.of("Unknown");
    }

    public int getWeight(@NotNull Class<? extends ICategory> categoryClass) {
        ICategory category = this.getInstance(categoryClass);
        return category != null ? category.getWeight() : 0;
    }

    private @Nullable ICategory getInstance(@NotNull Class<? extends ICategory> categoryClass) {
        return this.categories.stream().filter(category -> category.getClass().equals(categoryClass)).findFirst().orElse(null);
    }
}
