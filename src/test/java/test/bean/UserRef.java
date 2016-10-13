package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

/**
 * Created by akwei on 10/13/16.
 */
@Table(name = "userref")
public class UserRef {

    @Id
    @Column
    private int uid;

    @Id(1)
    @Column
    private int refid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRefid() {
        return refid;
    }

    public void setRefid(int refid) {
        this.refid = refid;
    }
}
