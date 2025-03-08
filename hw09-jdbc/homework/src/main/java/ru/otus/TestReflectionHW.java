package ru.otus;

import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

public class TestReflectionHW {
    public static void main(String[] args) {
        var dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Manager> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);

        entitySQLMetaDataClient.getUpdateSql();
    }
}
