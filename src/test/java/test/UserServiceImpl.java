package test;

import test.bean.User;

public class UserServiceImpl {

	public void createUserTx(User user) {
		user.create();
	}
}
