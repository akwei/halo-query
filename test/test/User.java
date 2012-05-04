package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 目前字段类型只支持long,int,byte,short,float,char,double,String,java.util.Date
 * 
 * @author akwei
 */
// @Table(name = "user",partitionid="userPart")
// //如果通过spring实例化分表分析器，只需要指定partitionid=beanid
// @Table(name = "user", partitionClass = TestUserDbPartitionHelper.class) //
// partitionClass=指定分表分析器类型
// @Table(name = "user", partitionClass =
// DbPartitionHelperDef.class)//默认使用不分表分库的分析器，写法同下
@Table(name = "user")
// 默认使用不分表分库的分析器
public class User {

    @Id
    @Column
    private long userid;

    @Column
    private BigInteger uuid;

    @Column
    private Double uuid2;

    @Column
    private double uuid3;

    @Column
    private float uuid4;

    @Column
    private Float uuid5;

    @Column
    private short uuid6;

    @Column
    private Short uuid7;

    @Column
    private byte uuid8;

    @Column
    private Byte uuid9;

    @Column
    private Long uuid10;

    @Column
    private int uuid11;

    @Column
    private BigDecimal uuid12;

    @Column
    // 标明是与数据库对应的列，如果与数据库对应的列写法不一样包括大小写，那么就需要这样写：@Column("db_user_nick")
    private String nick;

    @Column
    private String addr;

    @Column
    private String intro;

    @Column
    private Integer sex;

    @Column
    private Date createtime;

    public void setUuid(BigInteger uuid) {
        this.uuid = uuid;
    }

    public BigInteger getUuid() {
        return uuid;
    }

    public long getUserid() {
        return userid;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Double getUuid2() {
        return uuid2;
    }

    public void setUuid2(Double uuid2) {
        this.uuid2 = uuid2;
    }

    public double getUuid3() {
        return uuid3;
    }

    public void setUuid3(double uuid3) {
        this.uuid3 = uuid3;
    }

    public float getUuid4() {
        return uuid4;
    }

    public void setUuid4(float uuid4) {
        this.uuid4 = uuid4;
    }

    public Float getUuid5() {
        return uuid5;
    }

    public void setUuid5(Float uuid5) {
        this.uuid5 = uuid5;
    }

    public short getUuid6() {
        return uuid6;
    }

    public void setUuid6(short uuid6) {
        this.uuid6 = uuid6;
    }

    public Short getUuid7() {
        return uuid7;
    }

    public void setUuid7(Short uuid7) {
        this.uuid7 = uuid7;
    }

    public byte getUuid8() {
        return uuid8;
    }

    public void setUuid8(byte uuid8) {
        this.uuid8 = uuid8;
    }

    public Byte getUuid9() {
        return uuid9;
    }

    public void setUuid9(Byte uuid9) {
        this.uuid9 = uuid9;
    }

    public Long getUuid10() {
        return uuid10;
    }

    public void setUuid10(Long uuid10) {
        this.uuid10 = uuid10;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }

    public int getUuid11() {
        return uuid11;
    }

    public void setUuid11(int uuid11) {
        this.uuid11 = uuid11;
    }

    public BigDecimal getUuid12() {
        return uuid12;
    }

    public void setUuid12(BigDecimal uuid12) {
        this.uuid12 = uuid12;
    }
}