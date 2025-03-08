package ru.otus.jdbc.mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vavr.control.Try;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(),
            Collections.singletonList(id),
            rs -> Try.of(() -> {
                if (!rs.next()) return null;

                var arguments = entityClassMetaData
                        .getAllFields()
                        .stream()
                        .map(field -> Try.of(() -> rs.getObject(field.getName().toLowerCase())).get())
                        .toArray();

                return entityClassMetaData
                        .getConstructor()
                        .newInstance(arguments);
                }).get()
            );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> Try.of(() -> {
            var list = new ArrayList<T>();
            while (rs.next()) {
                var arguments = entityClassMetaData
                        .getAllFields()
                        .stream()
                        .map(field -> Try.of(() -> rs.getObject(field.getName().toLowerCase())).get()
                        )
                        .toArray();

                list.add(entityClassMetaData
                        .getConstructor()
                        .newInstance(arguments));
            }
            return list;
        }).get()).orElseThrow();
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
            entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> Try.of(() -> {
                    field.setAccessible(true);
                    return field.get(client);
                }).get())
                .collect(Collectors.toList())
        );
    }

    @Override
    public void update(Connection connection, T client) {
        var fieldValues = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> Try.of(() -> {
                    field.setAccessible(true);
                    return field.get(client);
                }).get());

        entityClassMetaData.getIdField().setAccessible(true);
        var fieldId = Try.of(() -> Stream.of(entityClassMetaData.getIdField().get(client))).get();

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                Stream.concat(fieldValues, fieldId).toList());
    }
}
