package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

import java.sql.Timestamp;

@Table(name = "ewallet.test3")
public class Test3 extends BaseModel {

	@Id
	@Column
	private int id;

	@Column
	private String name;

	@Column
	private Timestamp time;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Timestamp getTime() {
		return time;
	}
}