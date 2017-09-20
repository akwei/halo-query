package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

@Table(name = "minfo")
public class Minfo {

    @Id
    @Column
    private int tid;

    @Column
    private String name;

    @Column
    private String mkey;

    @Column
    private int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Minfo)) return false;

        Minfo minfo = (Minfo) o;

        if (getTid() != minfo.getTid()) return false;
        if (getSex() != minfo.getSex()) return false;
        if (getName() != null ? !getName().equals(minfo.getName()) : minfo.getName() != null) return false;
        return getMkey() != null ? getMkey().equals(minfo.getMkey()) : minfo.getMkey() == null;
    }
}
