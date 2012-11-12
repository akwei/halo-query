package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

@Table(name = "ewallet.test", dalParser = Db2Parser.class)
public class Db2TestModel extends BaseModel {

	@Id
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private Timestamp time1;

	@Column
	private Integer t2;

	@Column
	private String str;

	@Column
	private int t1;// not null

	@Column
	private Timestamp time2;// not null;

	public void setTime2(Timestamp time2) {
		this.time2 = time2;
	}

	public Timestamp getTime2() {
		return time2;
	}

	public int getT1() {
		return t1;
	}

	public void setT1(int t1) {
		this.t1 = t1;
	}

	public Integer getT2() {
		return t2;
	}

	public void setT2(Integer t2) {
		this.t2 = t2;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void setTime1(Timestamp time1) {
		this.time1 = time1;
	}

	public Timestamp getTime1() {
		return time1;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<Db2TestModel> getList() {
		return query.db2List(Db2TestModel.class, "", "", 0, 1, null,
		        new RowMapper<Db2TestModel>() {

			        public Db2TestModel mapRow(ResultSet rs, int rowNum)
			                throws SQLException {
				        Db2TestModel o = new Db2TestModel();
				        o.setId(rs.getLong("ewallet_test_id"));
				        o.setName(rs.getString("ewallet_test_name"));
				        o.setTime1(rs.getTimestamp("ewallet_test_time1"));
				        o.setTime2(rs.getTimestamp("ewallet_test_time2"));
				        return o;
			        }
		        });
	}

	public static List<Db2TestModel> getList2() {
		return Db2TestModel.db2List(null, null, 0, 1, null);
	}

	public static Db2TestModel getObj() {
		return Db2TestModel.obj("where id=?", new Object[] { 112 });
	}
}
