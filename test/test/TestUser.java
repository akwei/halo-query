package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

import java.util.Date;

@Table(name = "testuser")
public class TestUser {

    // 对应数据库user_id，如果字段与数据库列名相同可以不用写(name = "user_id")
    @Id
    @Column
    private long userid;

    // 对应数据库user_nick，如果字段与数据库列名相同可以不用写(name = "user_nick")
    @Column
    private String nick;

    @Column
    private Date createtime;

    @Column
    private byte gender;

    @Column
    private double money;

    @Column
    private float purchase;

    public long getUserid() {
        return userid;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public float getPurchase() {
        return purchase;
    }

    public void setPurchase(float purchase) {
        this.purchase = purchase;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}