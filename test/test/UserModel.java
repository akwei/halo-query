package test;

import halo.query.model.BaseModel;

public class UserModel extends BaseModel {
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
