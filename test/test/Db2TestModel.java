package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

@Table(name = "ewallet.test")
public class Db2TestModel extends BaseModel {

	@Id
	@Column
	private long id;

	@Column
	private String name;

	@Column
	private Timestamp time;

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
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
				        o.setId(rs.getInt("ewallet_test_id"));
				        o.setName(rs.getString("ewallet_test_name"));
				        o.setTime(rs.getTimestamp("ewallet_test_time"));
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
