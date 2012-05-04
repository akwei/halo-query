package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

@Table(name = "userinfo")
public class UserInfo {

    @Id
    @Column
    private long uid;

    @Column
    private String intro;

    @Column
    private long userid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }
}
