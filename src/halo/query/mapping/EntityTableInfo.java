package halo.query.mapping;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * 表的映射类
 * 
 * @author akwei
 * @param <T>
 */
public class EntityTableInfo<T> {

    /**
     * 表映射的类型
     */
    private Class<T> clazz;

    private String tableName;

    /**
     * 对应数据表中的所有字段
     */
    private final List<String> columnNames = new ArrayList<String>();

    /**
     * 表中的id字段
     */
    private String idColumnName;

    // private String deleteSQL;
    //
    // private String updateSQL;
    //
    // private String insertSQL;
    private Field idField;

    private final List<Field> tableFields = new ArrayList<Field>();

    private RowMapper<T> rowMapper;

    private SQLMapper<T> sqlMapper;

    /**
     * 类的属性名与数据表字段的对应key为field,value为column
     */
    private final Map<String, String> fieldColumnMap = new HashMap<String, String>();

    /**
     * 类的属性名与数据表字段的对应key为column,value为field
     */
    private final Map<String, Field> columnFieldMap = new HashMap<String, Field>();

    public EntityTableInfo(Class<T> clazz) {
        super();
        this.clazz = clazz;
        this.init();
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public String getDeleteSQL(String tablePostfix) {
        return this.buildDeleteSQL(tablePostfix);
    }

    public String getInsertSQL(String tablePostfix) {
        return this.buildInsertSQL(tablePostfix);
    }

    public String getUpdateSQL(String tablePostfix) {
        return this.buildUpdateSQL(tablePostfix);
    }

    public Field getIdField() {
        return idField;
    }

    public List<Field> getTableFields() {
        return tableFields;
    }

    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public SQLMapper<T> getSqlMapper() {
        return sqlMapper;
    }

    public boolean isIdField(Field field) {
        if (this.idField.equals(field)) {
            return true;
        }
        return false;
    }

    private void init() {
        this.buildTableName();
        this.buildColumnNames();
        this.buildIdColumn();
        this.createRowMapper();
        this.createSQLMapper();
        if (idField == null) {
            throw new RuntimeException("no id field for "
                    + this.clazz.getName());
        }
    }

    private String buildInsertSQL(String postfix) {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(this.tableName);
        if (postfix != null && postfix.trim().length() > 0) {
            sb.append(postfix);
        }
        sb.append("(");
        for (String col : columnNames) {
            sb.append(col);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        sb.append(" values");
        sb.append("(");
        int len = columnNames.size();
        for (int i = 0; i < len; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private String buildDeleteSQL(String postfix) {
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(this.tableName);
        if (postfix != null && postfix.trim().length() > 0) {
            sb.append(postfix);
        }
        sb.append(" where ");
        sb.append(this.idColumnName);
        sb.append("=?");
        return sb.toString();
    }

    private String buildUpdateSQL(String postfix) {
        StringBuilder sb = new StringBuilder("update ");
        sb.append(this.tableName);
        if (postfix != null && postfix.trim().length() > 0) {
            sb.append(postfix);
        }
        sb.append(" set ");
        for (String col : columnNames) {
            if (col.equals(idColumnName)) {
                continue;
            }
            sb.append(col);
            sb.append("=?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where ");
        sb.append(this.idColumnName);
        sb.append("=?");
        return sb.toString();
    }

    private void buildTableName() {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("tableName not set [ " + clazz.getName()
                    + " ]");
        }
        this.tableName = table.name();
    }

    private void buildColumnNames() {
        this.buildColumnNamesForClass(clazz);
    }

    private void buildColumnNamesForClass(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            this.buildColumnNamesForClass(superClazz);
        }
        Field[] fs = clazz.getDeclaredFields();
        Column column;
        for (Field f : fs) {
            column = f.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            f.setAccessible(true);
            tableFields.add(f);
            if (column.value().trim().length() == 0) {
                fieldColumnMap.put(f.getName(), f.getName());
                columnFieldMap.put(f.getName(), f);
                columnNames.add(f.getName());
            }
            else {
                fieldColumnMap.put(f.getName(), column.value().trim());
                columnFieldMap.put(column.value().trim(), f);
                columnNames.add(column.value().trim());
            }
        }
    }

    private void buildIdColumn() {
        Field[] fs = clazz.getDeclaredFields();
        Id id;
        for (Field f : fs) {
            id = f.getAnnotation(Id.class);
            if (id == null) {
                continue;
            }
            f.setAccessible(true);
            this.idField = f;
            Column column = f.getAnnotation(Column.class);
            if (column == null) {
                throw new RuntimeException(
                        "must has @Column annotation on field "
                                + clazz.getName() + "." + f.getName());
            }
            String value = column.value();
            if (value == null || value.trim().length() == 0) {
                idColumnName = f.getName();
            }
            else {
                idColumnName = column.value().trim();
            }
            break;
        }
    }

    /**
     * 获得数据库对应的列名称
     * 
     * @param fieldName
     *            java对象的字段名称
     * @return
     */
    public String getColumn(String fieldName) {
        return fieldColumnMap.get(fieldName);
    }

    public String getFullColumn(String fieldName) {
        if (this.tableName != null && this.tableName.length() > 0) {
            return this.tableName + "." + this.getColumn(fieldName);
        }
        return this.getColumn(fieldName);
    }

    public Field getField(String columnName) {
        return columnFieldMap.get(columnName);
    }

    private void createSQLMapper() {
        SQLMapperCreater creater = new SQLMapperCreater();
        Class<SQLMapper<T>> mapperClass = creater
                .createSqlUpdateMapperClass(this);
        try {
            this.sqlMapper = mapperClass.getConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createRowMapper() {
        RowMapperClassCreater creater = new RowMapperClassCreater();
        Class<RowMapper<T>> mapperClass = creater.createRowMapperClass(this);
        try {
            this.rowMapper = mapperClass.getConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
