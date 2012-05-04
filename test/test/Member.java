package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;

@Table(name = "member")
public class Member {

    @Id
    @Column("memberuserid")
    private long memberUserId;

    @Column
    private long userid;

    @Column
    private String nick;

    @Column
    private long groupid;

    private TestUser testUser;

    public long getMemberUserId() {
        return memberUserId;
    }

    public void setMemberUserId(long memberUserId) {
        this.memberUserId = memberUserId;
    }

    public void setTestUser(TestUser testUser) {
        this.testUser = testUser;
    }

    public TestUser getTestUser() {
        return testUser;
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

    public long getGroupid() {
        return groupid;
    }

    public void setGroupid(long groupid) {
        this.groupid = groupid;
    }
}
