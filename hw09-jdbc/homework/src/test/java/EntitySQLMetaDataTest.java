import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Manager;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

public class EntitySQLMetaDataTest {

    @Test
    public void shouldCorrectGetUpdateSql() {
        var actualSql = getEntitySQLMetaDataClient().getUpdateSql();
        var expectedSql = "update manager set label=? param1=? where no = ?";

        Assertions.assertEquals(expectedSql, actualSql);
    }

    @Test
    public void shouldCorrectGetSelectAllSql() {
        var actualSql = getEntitySQLMetaDataClient().getSelectAllSql();
        var expectedSql = "select * from manager";

        Assertions.assertEquals(expectedSql, actualSql);
    }

    @Test
    public void shouldCorrectGetSelectByIdSql() {
        var actualSql = getEntitySQLMetaDataClient().getSelectByIdSql();
        var expectedSql = "select * from manager where no in (?)";

        Assertions.assertEquals(expectedSql, actualSql);
    }

    @Test
    public void shouldCorrectGetInsertSql() {
        var actualSql = getEntitySQLMetaDataClient().getInsertSql();
        var expectedSql = "insert into manager(label,param1) values (?,?)";

        Assertions.assertEquals(expectedSql, actualSql);
    }

    private EntitySQLMetaData getEntitySQLMetaDataClient() {
        EntityClassMetaData<Manager> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Manager.class);
        return new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
    }
}
