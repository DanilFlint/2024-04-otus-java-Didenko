package ru.otus.jdbc.mapper.impl;

import ru.otus.crm.model.Client;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl implements EntityClassMetaData<Client> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Constructor<Client> getConstructor() {
        return null;
    }

    @Override
    public Field getIdField() {
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return null;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return null;
    }
}
