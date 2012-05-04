package halo.query.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RowMapperUtil {

    private RowMapperUtil() {
    }

    public static int getInt(ResultSet rs, String name) throws SQLException {
        return rs.getInt(name);
    }

    public static Integer getObjInt(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public static long getLong(ResultSet rs, String name) throws SQLException {
        return rs.getLong(name);
    }

    public static Long getObjLong(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Long.valueOf(value);
    }

    public static byte getByte(ResultSet rs, String name) throws SQLException {
        return rs.getByte(name);
    }

    public static Byte getObjByte(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Byte.valueOf(value);
    }

    public static short getShort(ResultSet rs, String name) throws SQLException {
        return rs.getShort(name);
    }

    public static Short getObjShort(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Short.valueOf(value);
    }

    public static float getFloat(ResultSet rs, String name) throws SQLException {
        return rs.getFloat(name);
    }

    public static Float getObjFloat(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Float.valueOf(value);
    }

    public static double getDouble(ResultSet rs, String name)
            throws SQLException {
        return rs.getDouble(name);
    }

    public static Double getObjDouble(ResultSet rs, String name)
            throws SQLException {
        String value = getString(rs, name);
        if (value == null || value.length() == 0) {
            return null;
        }
        return Double.valueOf(value);
    }

    public static BigDecimal getBigDecimal(ResultSet rs, String name)
            throws SQLException {
        return rs.getBigDecimal(name);
    }

    public static Timestamp getTimestamp(ResultSet rs, String name)
            throws SQLException {
        return rs.getTimestamp(name);
    }

    public static String getString(ResultSet rs, String name)
            throws SQLException {
        return rs.getString(name);
    }

    public static BigInteger getBigInteger(ResultSet rs, String name)
            throws SQLException {
        return new BigInteger(rs.getString(name));
    }
}
