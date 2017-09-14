package halo.query;

import halo.query.dal.DALInfo;
import halo.query.dal.DALParserUtil;
import halo.query.mapping.EntityTableInfo;
import halo.query.mapping.EntityTableInfoFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 对sql的处理
 * Created by akwei on 9/29/15.
 */
@SuppressWarnings("unchecked")
public class SqlBuilder {

    public static <T> String buildUpdateSQL(Class<T> clazz) {
        StringBuilder sb = new StringBuilder("update ");
        sb.append(getTableNameAndSetDsKey(clazz));
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        sb.append(" set ");
        List<String> cols = new ArrayList<>();
        List<String> columnNames = info.getColumnNames();
        for (String col : columnNames) {
            if (info.isIdColumnName(col)) {
                continue;
            }
            cols.add(col);
        }
        int i = 0;
        int lastIdx = cols.size() - 1;
        for (String col : cols) {
            sb.append(col);
            sb.append("=?");
            if (i < lastIdx) {
                sb.append(',');
            }
            i++;
        }
        sb.append(" where ");
        if (info.getIdColumnNames().isEmpty()) {
            throw new HaloIdException(clazz.getName() + " must has id when build object update sql");
        }
        buildIdSQLPart(sb, info);
        return sb.toString();
    }

    /**
     * 创建insert sql 支持 replace into, insert ignore into, insert into, insert into ... on duplicate key update
     *
     * @param clazz       实体类型
     * @param hasIdColumn 是否包含id字段
     * @param insertFlag  0:insert into 1:replace into 2:insert ignore
     * @param updateCols  当进行 insert into on duplicate key update 时，需要更新的数据表列的名字
     * @param <T>         泛型
     * @return insert sql
     */
    public static <T> String buildInsertSQL(Class<T> clazz, boolean hasIdColumn, InsertFlag insertFlag, String[] updateCols) {
        boolean _hasIdColumn = hasIdColumn;
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        if (info.getIdFields().size() > 1) {
            _hasIdColumn = true;
        }
        StringBuilder sb = new StringBuilder();
        if (insertFlag.equals(InsertFlag.INSERT_INTO)) {
            sb.append("insert into ");
        } else if (insertFlag.equals(InsertFlag.REPLACE_INTO)) {
            sb.append("replace into ");
        } else if (insertFlag.equals(InsertFlag.INSERT_IGNORE_INTO)) {
            sb.append("insert ignore into ");
        } else if (insertFlag.equals(InsertFlag.INSERT_INTO_ON_DUPLICATE_KEY_UPDATE)) {
            sb.append("insert into ");
        } else {
            throw new RuntimeException("insertFlag[" + insertFlag + "] not supported");
        }
        String tableName = getTableNameAndSetDsKey(clazz);
        sb.append(tableName);
        sb.append('(');
        List<String> cols = new ArrayList<>();
        List<String> columnNames = info.getColumnNames();
        for (String col : columnNames) {
            if (!_hasIdColumn && info.isIdColumnName(col)) {
                continue;
            }
            cols.add(col);
        }

        int k = 0;
        int lastIdx = cols.size() - 1;
        for (String col : cols) {
            sb.append(col);
            if (k < lastIdx) {
                sb.append(',');
            }
            k++;
        }
        sb.append(')');
        sb.append(" values");
        sb.append('(');
        int len = cols.size();
        lastIdx = len - 1;
        for (int i = 0; i < len; i++) {
            sb.append('?');
            if (i < lastIdx) {
                sb.append(',');
            }
        }
        sb.append(')');
        if (insertFlag.equals(InsertFlag.INSERT_INTO_ON_DUPLICATE_KEY_UPDATE) && updateCols != null && updateCols.length != 0) {
            sb.append(" on duplicate key update ");
            int j = 0;
            int updateColsLastIdx = updateCols.length - 1;
            for (String updateCol : updateCols) {
                sb.append(updateCol).append("=values(").append(updateCol).append(")");
                if (j < updateColsLastIdx) {
                    sb.append(",");
                }
                j++;
            }
        }
        return sb.toString();
    }

    public static <T> String buildDeleteSQL(Class<T> clazz) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ").append(getTableNameAndSetDsKey(clazz)).append(" where ");
        if (info.getIdColumnNames().isEmpty()) {
            throw new HaloIdException(clazz.getName() + " must has id when build object delete sql");
        }
        buildIdSQLPart(sb, info);
        return sb.toString();
    }

    public static String buildCountSQL(Class<?>[] clazzes, String afterFrom) {
        StringBuilder sb = new StringBuilder("select count(*) from ");
        addTableNameAndSetDsKey(sb, clazzes, true);
        sb.append(' ');
        sb.append(afterFrom);
        return sb.toString();
    }

    public static <T> String buildCountSQL(Class<T> clazz, String afterFrom) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        addTableNameAndSetDsKey(sb, clazz, true, false);
        sb.append(' ');
        if (afterFrom != null) {
            sb.append(afterFrom);
        }
        return sb.toString();
    }

    public static <T> String buildListSQL(Class<T> clazz, String afterFrom) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(info.getSelectedFieldSQL());
        sb.append(" from ");
        addTableNameAndSetDsKey(sb, clazz, true, false);
        sb.append(' ');
        if (afterFrom != null) {
            sb.append(afterFrom);
        }
        return sb.toString();
    }

    public static <T> String buildMysqlListSQL(Class<?>[] clazzes, String afterFrom, int begin, int size) {
        StringBuilder sb = new StringBuilder("select ");
        EntityTableInfo<T> info;
        int i = 0;
        for (Class<?> clazz : clazzes) {
            info = getEntityTableInfo(clazz);
            sb.append(info.getSelectedFieldSQL());
            if (i < clazzes.length - 1) {
                sb.append(',');
            }
            i++;
        }
        sb.append(" from ");
        addTableNameAndSetDsKey(sb, clazzes, true);
        sb.append(' ');
        sb.append(afterFrom);
        buildLimitPart(sb, begin, size);
        return sb.toString();
    }

    public static <T> String buildMysqlListSQL(Class<T> clazz, String afterFrom, int begin, int size) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(info.getSelectedFieldSQL());
        sb.append(" from ");
        addTableNameAndSetDsKey(sb, clazz, true, false);
        sb.append(' ');
        if (afterFrom != null) {
            sb.append(afterFrom);
        }
        buildLimitPart(sb, begin, size);
        return sb.toString();
    }

    public static <T> String buildDeleteSQL(Class<T> clazz, String afterFrom) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        addTableNameAndSetDsKey(sb, clazz, false, false);
        sb.append(' ');
        if (afterFrom != null) {
            sb.append(afterFrom);
        }
        return sb.toString();
    }

    public static <T> String buildUpdateSQL(Class<T> clazz, String updateSqlSeg) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        addTableNameAndSetDsKey(sb, clazz, false, false);
        sb.append(' ');
        sb.append(updateSqlSeg);
        return sb.toString();
    }

    public static <T> String buildObjByIdsSQLSeg(Class<T> clazz, Object[] idValues, boolean forUpdate) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        int idSize = info.getIdColumnNames().size();
        if (idValues.length != idSize) {
            throw new RuntimeException(clazz.getName() + " has " + idSize + " id. " + "please input " + idSize + " arguments");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("where ");
        buildIdSQLPart(sb, info);
        if (forUpdate) {
            sb.append(" for update");
        }
        return sb.toString();
    }

    public static String createInSql(String column, int argCount) {
        if (argCount <= 0) {
            throw new IllegalArgumentException("argCount must be > 0");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(" in(");
        int lastIdx = argCount - 1;
        for (int i = 0; i < argCount; i++) {
            sb.append('?');
            if (i < lastIdx) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * 根据快照，生成update sql，sql中只包含更新的数据
     *
     * @param t        要更新的数据
     * @param snapshot 更新前的快照
     * @param cas      是否使用cas操作
     * @param <T>      泛型
     * @return null 没有值的改变,因此不产生更新sql。返回对象表示有更新数据
     */
    public static <T> UpdateSnapshotInfo buildUpdateSegSQLForSnapshot(T t, T snapshot, boolean cas) {
        StringBuilder sb = new StringBuilder("set ");
        EntityTableInfo<T> entityTableInfo = getEntityTableInfo(t.getClass());
        List<String> cols = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        long oldCasValue = 0;
        if (cas) {
            oldCasValue = entityTableInfo.setCasFieldValue(t, entityTableInfo.getCasField(), true);
        }
        try {
            if (snapshot == null) {
                for (Field field : entityTableInfo.getTableFields()) {
                    Object valueT = field.get(t);
                    if (entityTableInfo.isIdField(field)) {
                        continue;
                    }
                    values.add(valueT);
                    cols.add(entityTableInfo.getColumn(field.getName()));
                }
            } else {
                int sum = 0;
                for (Field field : entityTableInfo.getTableFields()) {
                    Object valueT = field.get(t);
                    Object valueSnapshootObj = field.get(snapshot);
                    if (entityTableInfo.isIdField(field)) {
                        continue;
                    }
                    boolean canAddSum = false;
                    if (valueT != null && valueSnapshootObj != null && !valueT.equals(valueSnapshootObj)) {
                        canAddSum = true;
                    } else if ((valueT == null && valueSnapshootObj != null) || (valueT != null && valueSnapshootObj == null)) {
                        canAddSum = true;
                    }
                    if (canAddSum) {
                        sum++;
                        values.add(valueT);
                        cols.add(entityTableInfo.getColumn(field.getName()));
                    }
                }
                if (sum == 0) {
                    if (cas) {
                        entityTableInfo.setCasFieldValue(t, entityTableInfo.getCasField(), false);
                    }
                    return null;
                }
            }
            int i = 0;
            int lastIdx = cols.size() - 1;
            for (String col : cols) {
                sb.append(col).append("=?");
                if (i < lastIdx) {
                    sb.append(',');
                }
                i++;
            }
            sb.append(" where ");
            if (entityTableInfo.getIdColumnNames().size() == 0) {
                if (cas) {
                    entityTableInfo.setCasFieldValue(t, entityTableInfo.getCasField(), false);
                }
                throw new HaloIdException(t.getClass().getName() + " must has id when update(T t, T snapshot)");
            }
            i = 0;
            for (String idColumnName : entityTableInfo.getIdColumnNames()) {
                values.add(entityTableInfo.getField(idColumnName).get(t));
                sb.append(idColumnName).append("=?");
                if (i < entityTableInfo.getIdColumnNames().size() - 1) {
                    sb.append(" and ");
                }
                i++;
            }
            if (cas) {
                sb.append(" and ").append(entityTableInfo.getCasColName()).append("=?");
                values.add(oldCasValue);
            }
            UpdateSnapshotInfo info = new UpdateSnapshotInfo();
            info.setSqlSeg(sb.toString());
            info.setValues(values);
            return info;
        } catch (IllegalAccessException e) {
            if (cas) {
                entityTableInfo.setCasFieldValue(t, entityTableInfo.getCasField(), false);
            }
            throw new RuntimeException(e);
        }
    }

    private static <T> EntityTableInfo<T> getEntityTableInfo(Class<?> clazz) {
        return (EntityTableInfo<T>) EntityTableInfoFactory.getEntityTableInfo(clazz);
    }

    private static <T> String getTableNameAndSetDsKey(Class<T> clazz) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        DALInfo dalInfo = DALParserUtil.process(clazz, info.getDalParser());
        if (dalInfo == null) {
            return info.getTableName();
        } else {
            String realTableName = dalInfo.getRealTable(clazz);
            if (realTableName == null) {
                return info.getTableName();
            }
            return realTableName;
        }
    }


    private static <T> void addTableNameAndSetDsKey(StringBuilder sb, Class<T> clazz, boolean addTableAlias, boolean addComma) {
        EntityTableInfo<T> info = getEntityTableInfo(clazz);
        DALInfo dalInfo = DALParserUtil.process(clazz, info.getDalParser());
        if (dalInfo == null) {
            sb.append(info.getTableName());
        } else {
            String realTableName = dalInfo.getRealTable(clazz);
            if (realTableName == null) {
                sb.append(info.getTableName());
            } else {
                sb.append(realTableName);
            }

        }
        if (addTableAlias) {
            sb.append(" as ");
            sb.append(info.getTableAlias());
        }
        if (addComma) {
            sb.append(',');
        }
    }

    private static void addTableNameAndSetDsKey(StringBuilder sb, Class<?>[] clazzes, boolean addTableAlias) {
        int i = 0;
        int lastIdx = clazzes.length - 1;
        for (Class<?> clazz : clazzes) {
            boolean addComma = true;
            if (i == lastIdx) {
                addComma = false;
            }
            addTableNameAndSetDsKey(sb, clazz, addTableAlias, addComma);
            i++;
        }
    }

    private static void buildLimitPart(StringBuilder sb, int begin, int size) {
        if (size > 0) {
            sb.append(" limit ");
            sb.append(begin);
            sb.append(',');
            sb.append(size);
        }
    }

    private static <T> void buildIdSQLPart(StringBuilder sb, EntityTableInfo<T> info) {
        int k = 0;
        for (String idColumnName : info.getIdColumnNames()) {
            sb.append(idColumnName).append("=?");
            if (k < info.getIdColumnNames().size() - 1) {
                sb.append(" and ");
            }
            k++;
        }
    }
}
