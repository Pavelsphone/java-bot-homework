package org.homework.di;

import org.homework.di.annotations.Component;
import org.homework.di.annotations.Inject;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DIContainer {
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final String basePackage;

    public DIContainer(String basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    private void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);

        for (Class<?> clazz : components) {
            try {
                singletons.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать экземпляр класса: " + clazz.getName(), e);
            }
        }

        injectDependencies();
    }

    private void injectDependencies() {
        for (Object instance : singletons.values()) {
            Class<?> clazz = instance.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> fieldType = field.getType();
                    Object dependency = singletons.get(fieldType);
                    if (dependency != null) {
                        field.setAccessible(true);
                        try {
                            field.set(instance, dependency);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Ошибка внедрения зависимости в поле: " + field.getName(), e);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        return (T) singletons.get(type);
    }
}
