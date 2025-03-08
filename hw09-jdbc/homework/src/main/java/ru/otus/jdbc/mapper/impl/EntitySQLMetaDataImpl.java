package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        String tableName = entityClassMetaData.getName().toLowerCase();
        String idFieldName = entityClassMetaData.getIdField().getName().toLowerCase();

        return String.format("select * from %s where %s in (?)", tableName, idFieldName);
    }

    @Override
    public String getInsertSql() {
        var tableName = entityClassMetaData.getName().toLowerCase();
        var fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName).collect(Collectors.joining(","));
        var params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> "?").collect(Collectors.joining(","));

        return String.format("insert into %s(%s) values (%s)", tableName, fields, params);
    }

    @Override
    public String getUpdateSql() {
        String tableName = entityClassMetaData.getName().toLowerCase();

        var setString = entityClassMetaData.getFieldsWithoutId().stream()
                .map((field) -> String.format("%s=?", field.getName().toLowerCase()))
                .collect(Collectors.joining(" "));
        var idFieldName = entityClassMetaData.getIdField().getName().toLowerCase();

        return String.format("update %s set %s where %s = ?", tableName, setString, idFieldName);
    }
}
