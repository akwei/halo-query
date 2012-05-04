package halo.query.mapping;

import halo.query.SQLMapper;
import halo.query.SQLMapperCreater;
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
    private String[] columnNames;

    /**
     * 表中的id字段
     */
    private String idColumnName;

    private String deleteSQL;

    private String updateSQL;

    private String insertSQL;

    private Field idField;

    private Field[] tableFields;

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

    public String[] getColumnNames() {
        return columnNames;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public String getDeleteSQL() {
        return deleteSQL;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getUpdateSQL() {
        return updateSQL;
    }

    public Field getIdField() {
        return idField;
    }

    public Field[] getTableFields() {
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
        this.buildInsertSQL();
        this.buildUpdateSQL();
        this.buildDeleteSQL();
        this.createRowMapper();
        this.createSQLMapper();
        if (idField == null) {
            throw new RuntimeException("no id field for "
                    + this.clazz.getName());
        }
    }

    private void buildInsertSQL() {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(this.tableName);
        sb.append("(");
        for (String col : columnNames) {
            sb.append(col);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        sb.append(" values");
        sb.append("(");
        int len = columnNames.length;
        for (int i = 0; i < len; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        this.insertSQL = sb.toString();
    }

    private void buildDeleteSQL() {
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(this.tableName);
        sb.append(" where ");
        sb.append(this.idColumnName);
        sb.append("=?");
        this.deleteSQL = sb.toString();
    }

    private void buildUpdateSQL() {
        StringBuilder sb = new StringBuilder("update ");
        sb.append(this.tableName);
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
        this.updateSQL = sb.toString();
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
        Field[] fs = clazz.getDeclaredFields();
        Column column;
        List<Field> fieldList = new ArrayList<Field>();
        List<String> list = new ArrayList<String>();
        for (Field f : fs) {
            column = f.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            f.setAccessible(true);
            fieldList.add(f);
            if (column.value().trim().length() == 0) {
                fieldColumnMap.put(f.getName(), f.getName());
                columnFieldMap.put(f.getName(), f);
                list.add(f.getName());
            }
            else {
                fieldColumnMap.put(f.getName(), column.value().trim());
                columnFieldMap.put(column.value().trim(), f);
                list.add(column.value().trim());
            }
        }
        this.columnNames = list.toArray(new String[list.size()]);
        this.tableFields = fieldList.toArray(new Field[fieldList.size()]);
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
            idColumnName = f.getName();
            this.idField = f;
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
