package test.bean;

import halo.query.Query;
import halo.query.dal.DALInfo;
import halo.query.dal.DALParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by akwei on 11/7/15.
 */
public class UserSeqUtil {

    private static final String VALUE_SQL = "select last_insert_id()";

    @Autowired
    private Query query;

    public int nextKey() {
        DALInfo dalInfo = DALParserUtil.process(UserSeq.class);
        final String tname = dalInfo.getRealTable(UserSeq.class);
        return query.getJdbcSupport().execute(new ConnectionCallback<Integer>() {
            @Override
            public Integer doInConnection(Connection con) throws SQLException,
                    DataAccessException {
                int tid = 0;
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = con.createStatement();
                    stmt.executeUpdate("update " + tname + " set seq_id = " +
                            "last_insert_id(seq_id + 1)");
                    rs = stmt.executeQuery(VALUE_SQL);
                    if (!rs.next()) {
                        throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                    }
                    tid = rs.getInt(1);
                } finally {
                    JdbcUtils.closeStatement(stmt);
                    JdbcUtils.closeResultSet(rs);
                }
                return tid;
            }
        });
    }
}
