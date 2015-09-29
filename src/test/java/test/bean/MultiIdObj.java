package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

/**
 * Created by akwei on 9/29/15.
 */
@Table(name = "multiidobj")
public class MultiIdObj {

    @Id(0)
    @Column
    private int uid;

    @Id(1)
    @Column
    private String oid;

    @Column("create_time")
    private long createTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
