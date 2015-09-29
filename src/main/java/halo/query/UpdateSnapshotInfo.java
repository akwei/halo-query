package halo.query;

import java.util.List;

/**
 * Created by akwei on 9/30/15.
 */
public class UpdateSnapshotInfo {

    private String sqlSeg;

    private List<Object> values;

    public String getSqlSeg() {
        return sqlSeg;
    }

    public void setSqlSeg(String sqlSeg) {
        this.sqlSeg = sqlSeg;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
