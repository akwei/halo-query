package halo.query.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface HaloRowMapper<T> {

    T mapRow(String alias, ResultSet rs, int rowNum) throws SQLException;
}
