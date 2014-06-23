package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.dal.DALInfo;
import halo.query.dal.DALStatus;
import halo.query.model.BaseModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "testuser", dalParser = TestUser2Parser.class)
public class TestUser2 extends BaseModel {

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

    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

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

    public static List<TestUser2> getListByGender(byte gender, int begin,
                                                  int size) throws Exception {
        return TestUser2.mysqlList("where gender=?", begin, size,
                new Object[]{gender});
    }

    @Override
    public void create() {
        DALStatus.setDsKey("ds_mysql");
        DALInfo dalInfo = new DALInfo();
        dalInfo.setRealTable(TestUser2.class, "testuser");
        DALStatus.setDalInfo(TestUser2.class, dalInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param0", 11);
        map.put("param1", "hello");
        DALStatus.setParamMap(map);
        super.create();
    }
}