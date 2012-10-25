package test.bean;

import halo.query.mapping.RowMapperUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {

    public UserMapper() {
    }

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setAddr(RowMapperUtil.getString(rs, "addr"));
        user.setUserid(RowMapperUtil.getLong(rs, "userid"));
        user.setUuid(RowMapperUtil.getBigInteger(rs, "uuid"));
        return user;
    }
}