package halo.query.idtool;

import halo.query.Query;
import halo.query.dal.DALInfo;
import halo.query.mapping.EntityTableInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.incrementer.AbstractColumnMaxValueIncrementer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * mysql自增表的id必须是myISAM类型，为了保证不同请求都能获得唯一id
 *
 * @author akwei
 */
public class HaloMySQLMaxValueIncrementer extends
        AbstractColumnMaxValueIncrementer {

    private static final String VALUE_SQL = "select last_insert_id()";

    public HaloMySQLMaxValueIncrementer(DataSource dataSource,
                                        String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
    }

    private EntityTableInfo<?> entityTableInfo;

    public void setEntityTableInfo(EntityTableInfo<?> entityTableInfo) {
        this.entityTableInfo = entityTableInfo;
    }

    @Override
    protected long getNextKey() throws DataAccessException {
        Class<?> clazz = entityTableInfo.getSeqDalParser().getClass();
        DALInfo dalInfo = Query.process(clazz, entityTableInfo.getSeqDalParser());
        String realName = null;
        if (dalInfo != null) {
            realName = dalInfo.getRealTable(clazz);
        }
        if (realName == null) {
            realName = this.getIncrementerName();
        }
        final String tableName = realName;
        long tid = Query.getInstance().getJdbcSupport().getJdbcTemplate().execute
                (new
                         ConnectionCallback<Long>() {
                             @Override
                             public Long doInConnection(Connection connection) throws SQLException, DataAccessException {
                                 long tid = 0;
                                 Statement stmt = null;
                                 ResultSet rs = null;
                                 String columnName = getColumnName();
                                 try {
                                     stmt = connection.createStatement();
                                     stmt.executeUpdate("update " + tableName + " set " + columnName + " = last_insert_id(" + columnName + " + " + getCacheSize() + ")");
                                     rs = stmt.executeQuery(VALUE_SQL);
                                     if (!rs.next()) {
                                         throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                                     }
                                     tid = rs.getLong(1);
                                 } finally {
                                     JdbcUtils.closeStatement(stmt);
                                     JdbcUtils.closeResultSet(rs);
                                 }
                                 return tid;
                             }
                         });
        return tid;
    }
}
