package test.bean;

import halo.query.annotation.Column;

public class BaseRole {

    @Column
    private String descr="";

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }
}