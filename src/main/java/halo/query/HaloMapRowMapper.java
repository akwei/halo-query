package halo.query;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 对tinyint特殊处理
 * Created by akwei on 10/18/15.
 */
public class HaloMapRowMapper extends ColumnMapRowMapper {

    private static final String TINYINT = "TINYINT";

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> mapOfColValues = createColumnMap(columnCount);
        String columnTypeName;
        for (int i = 1; i <= columnCount; i++) {
            String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            columnTypeName = rsmd.getColumnTypeName(i);
            Object obj;
            if (columnTypeName.startsWith(TINYINT)) {
                obj = rs.getInt(i);
            } else {
                obj = getColumnValue(rs, i);
            }
            mapOfColValues.put(key, obj);
        }
        return mapOfColValues;
    }
}
