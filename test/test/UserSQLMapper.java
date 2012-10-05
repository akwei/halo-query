package test;

import halo.query.mapping.SQLMapper;

public class UserSQLMapper implements SQLMapper {

	public Object[] getParamsForInsert(Object t) {
		test.TestUser o = (test.TestUser) t;
		return new Object[] { o.getUserid(), o.getNick(), o.getCreatetime(),
				o.getGender(), o.getMoney(), o.getPurchase() };
	}

	public Object[] getParamsForUpdate(Object t) {
		test.TestUser o = (test.TestUser) t;
		return new Object[] { o.getUserid(), o.getNick(), o.getCreatetime(),
				o.getGender(), o.getMoney(), o.getPurchase(), o.getUserid() };
	}

	public Object getIdParam(Object t) {
		test.TestUser o = (test.TestUser) t;
		return o.getUserid();
	}
}
