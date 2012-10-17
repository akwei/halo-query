package test;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;
import halo.query.model.HaloModel;

@HaloModel
@Table(name = "ewallet.test")
public class Db2TestModel extends BaseModel {

	@Id
	@Column
	private int id;

	@Column
	private String name;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
