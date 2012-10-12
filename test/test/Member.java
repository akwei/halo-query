package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.RefKey;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;
import halo.query.model.HaloModel;

import java.util.List;

@HaloModel
@Table(name = "member")
public class Member extends BaseModel {

	@Id
	@Column("memberuserid")
	private long memberUserId;

	@RefKey(refClass = TestUser.class)
	// 表示字段为与T1所对应表的逻辑外键，在使用 inner join查询时的关联条件，例如where
	// table_1.user_id=table_2.user_id
	@Column
	private long userid;

	@Column
	private String nick;

	@Column
	private long groupid;

	private TestUser testUser;// 如果定义了RefKey，那么必须有一个T1.class的field，默认的写法，开头字母小写的命名方式

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

	public List<Member> getJoinList(long userid) throws Exception {
		return query
				.mysqlListMulti(
						new Class[] { Member.class,
								TestUser.class
						},
						new String[] { null, "00" },
						"where testuser.userid=member.userid and member.userid=? order by member.userid asc",
						0, 1,
						new Object[] { userid });
	}
}
