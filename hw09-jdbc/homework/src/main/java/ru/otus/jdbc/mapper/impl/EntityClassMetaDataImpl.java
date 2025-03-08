package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.crm.annotation.Id;

import java.lang.reflect.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            var clazzOfFields = getAllFields().stream().map(Field::getType).toList();
            Class<?>[] clazzOfFieldsArr = new Class<?>[clazzOfFields.size()];
            for (int i = 0; i < clazzOfFields.size(); i++) {
                clazzOfFieldsArr[i] = clazzOfFields.get(i);
            }
            return clazz.getConstructor(clazzOfFieldsArr);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) != null)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) == null)
                .collect(Collectors.toList());
    }
}
