package halo.query.mapping;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.dal.DALParser;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 表与实体类的映射信息类,此对象的所有操作请在同一个线程完成，本类的所有操作非线程安全
 *
 * @param <T> 对象泛型
 * @author akwei
 */
public class EntityTableInfo<T> {

    /**
     * 表映射的类型
     */
    private Class<T> clazz;

    private Constructor<T> constructor;

    private DALParser dalParser;

    private DALParser seqDalParser;

    private String tableName;

    /**
     * 表的别名
     */
    private String tableAlias;

    /**
     * 对应数据表中的所有字段
     */
    private final List<String> columnNames = new ArrayList<String>();

    private String selectedFieldSQL;

    /**
     * 主键的field
     */
    private List<Field> idFields;

    private List<String> idColumnNames;

    /**
     * 保存对应数据库字段的field集合
     */
    private final List<Field> tableFields = new ArrayList<Field>();

    private RowMapper<T> rowMapper;

    private SQLMapper<T> sqlMapper;

    public DALParser getDalParser() {
        return dalParser;
    }

    /**
     * 类的属性名与数据表字段的对应key为field,value为column
     */
    private final Map<String, String> fieldColumnMap = new HashMap<String, String>();

    private final Map<String, Field> columnFieldMap = new HashMap<String, Field>();

    private String columnNamePostfix;

    public EntityTableInfo(Class<T> clazz) {
        super();
        this.clazz = clazz;
        this.init();
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public DALParser getSeqDalParser() {
        return seqDalParser;
    }

    public void setSeqDalParser(DALParser seqDalParser) {
        this.seqDalParser = seqDalParser;
    }

    public List<Field> getIdFields() {
        return idFields;
    }

    public List<String> getIdColumnNames() {
        return idColumnNames;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
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

    public String getSelectedFieldSQL() {
        return this.selectedFieldSQL;
    }


    /**
     * 获得所有与数据库对应的field
     *
     * @return field list
     */
    public List<Field> getTableFields() {
        return tableFields;
    }

    /**
     * 获得spring RowMapper对象
     *
     * @return mapper list
     */
    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    /**
     * 获得 SQLMapper对象
     *
     * @return sql mapper
     */
    public SQLMapper<T> getSqlMapper() {
        return sqlMapper;
    }

    /**
     * 是否是id的field
     *
     * @param field 字段
     * @return true/false
     */
    public boolean isIdField(Field field) {
        if (this.idFields.contains(field)) {
            return true;
        }
        return false;
    }

    public boolean isIdColumnName(String columnName) {
        if (this.idColumnNames.contains(columnName)) {
            return true;
        }
        return false;
    }

    private void init() {
        this.buildConstructor();
        this.buildTable();
        this.buildFields();
        this.buildIdColumn();
        this.buildSelectedFieldSQL();
        this.createRowMapper();
        this.createSQLMapper();
//        if (this.idFields.isEmpty()) {
//            throw new RuntimeException("no id field for " + this.clazz.getName());
//        }
    }

    private void buildConstructor() {
        try {
            this.constructor = this.clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建select的字段sql片段
     */
    private void buildSelectedFieldSQL() {
        StringBuilder sb = new StringBuilder();
        for (String col : columnNames) {
            sb.append(this.tableAlias);
            sb.append(".");
            sb.append(col);
            sb.append(" as ");
            sb.append(this.getColumnAlias(col));
            sb.append(",");
        }
        if (!columnNames.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        this.selectedFieldSQL = sb.toString();
    }

    /**
     * 初始化表信息
     */
    private void buildTable() {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("tableName not set [ " + clazz.getName()
                    + " ]");
        }
        this.tableName = table.name();
        if (this.tableName == null || this.tableName.trim().length() == 0) {
            throw new RuntimeException("tableName not set [ " + clazz.getName()
                    + " ]");
        }
        this.tableAlias = this.tableName.replaceAll("\\.", "_") + "_";
        try {
            this.dalParser = (DALParser) (table.dalParser().getConstructor()
                    .newInstance());
        } catch (Exception e) {
            throw new RuntimeException("dalParser init error", e);
        }
        this.columnNamePostfix = "";
    }

    private void buildFields() {
        this.buildFieldsForClass(clazz);
    }

    /**
     * 检测类和父类的所有字段，获得表对应的field,以及有逻辑外键引用的field
     *
     * @param clazz 被检查的类
     */
    private void buildFieldsForClass(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            this.buildFieldsForClass(superClazz);
        }
        Field[] fs = clazz.getDeclaredFields();
        Column column;
        for (Field f : fs) {
            f.setAccessible(true);
            column = f.getAnnotation(Column.class);
            // 如果有Column annotation，field就是与数据表对应的字段
            if (column != null) {
                FieldTypeUtil.checkFieldType(this.clazz, f);
                tableFields.add(f);
                if (column.value().trim().length() == 0) {
                    fieldColumnMap.put(f.getName(), f.getName());
                    columnFieldMap.put(f.getName(), f);
                    columnNames.add(f.getName());
                } else {
                    fieldColumnMap.put(f.getName(), column.value().trim());
                    columnFieldMap.put(column.value().trim(), f);
                    columnNames.add(column.value().trim());
                }
            }
        }
    }

    /**
     * 检测表的主键field
     */
    private void buildIdColumn() {
        List<IdFieldObject> list = new ArrayList<IdFieldObject>(2);
        //        Field[] fs = clazz.getDeclaredFields();
        Id id;
        for (Field f : this.tableFields) {
            id = f.getAnnotation(Id.class);
            if (id == null) {
                continue;
            }
            f.setAccessible(true);
            list.add(new IdFieldObject(f, id.value(), this.getColumnValue(f)));
        }
        Collections.sort(list, new Comparator<IdFieldObject>() {
            @Override
            public int compare(IdFieldObject idFieldObject, IdFieldObject idFieldObject2) {
                if (idFieldObject.sort < idFieldObject2.sort) {
                    return -1;
                } else if (idFieldObject.sort == idFieldObject2.sort) {
                    throw new RuntimeException(idFieldObject.field.getName()
                            + "[" + idFieldObject.sort + "] , " +
                            "" + idFieldObject2.field.getName() + "[" +
                            idFieldObject2.sort + "]" + " must has different " +
                            "id index");
                }
                return 1;
            }
        });
        this.idFields = new ArrayList<Field>(2);
        this.idColumnNames = new ArrayList<String>(2);
        for (IdFieldObject idFieldObject : list) {
            this.idFields.add(idFieldObject.field);
            this.idColumnNames.add(idFieldObject.columnName);
        }
    }

    private String getColumnValue(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            throw new RuntimeException("must has @Column annotation on " +
                    "field " + clazz.getName() + "." + field.getName());
        }
        String value = column.value();
        if (value == null || value.trim().length() == 0) {
            return field.getName();
        }
        return column.value().trim();
    }

    /**
     * 获得数据库对应的列名称
     *
     * @param fieldName java对象的字段名称
     * @return 数据库 column name
     */
    public String getColumn(String fieldName) {
        return fieldColumnMap.get(fieldName);
    }

    /**
     * 数据表 column 对应的 field
     *
     * @param columnName 数据表中的列
     * @return field name
     */
    public Field getField(String columnName) {
        return columnFieldMap.get(columnName);
    }

    public Object getFieldValue(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得列名称的别名，表示为table_column
     *
     * @param fieldName 字段名
     * @return 数据表列别名
     */
    public String getColumnAliasByFieldName(String fieldName) {
        return this.tableAlias + this.getColumn(fieldName) + this.columnNamePostfix;
    }

    public String getColumnAlias(String columnName) {
        return this.tableAlias + columnName + this.columnNamePostfix;
    }

    private void createSQLMapper() {
        if (this.getTableFields().isEmpty()) {
            throw new RuntimeException("no any field in " + this.clazz.getName());
        }
        JavassitSQLMapperClassCreater creater = new JavassitSQLMapperClassCreater<T>(this);
        Class<SQLMapper<T>> mapperClass = creater.getMapperClass();
        try {
            this.sqlMapper = mapperClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createRowMapper() {
        JavassitRowMapperClassCreater creater = new JavassitRowMapperClassCreater<T>(this);
        Class<RowMapper<T>> mapperClass = creater.getMapperClass();
        try {
            this.rowMapper = mapperClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class IdFieldObject {

        /**
         * id字段
         */
        Field field;

        /**
         * 设置的顺序
         */
        int sort;

        /**
         * id 对应数据库中的字段
         */
        String columnName;

        IdFieldObject(Field field, int sort, String columnName) {
            this.field = field;
            this.sort = sort;
            this.columnName = columnName;
        }
    }
}
