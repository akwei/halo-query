package test.mysql;

import halo.query.Query;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import test.SuperBaseModelTest;
import test.bean.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
@Transactional
public class RoleTest extends SuperBaseModelTest {

	@Resource
	Query query;

	int roleId;

	Role role;

	@Before
	public void before() {
		role = new Role();
		role.setCreateTime(new Date());
		try {
			roleId = query.insertForNumber(role).intValue();
			role.setRoleId(roleId);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void update() {
		try {
			int result = query.update(role);
			Assert.assertEquals(1, result);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void deleteById() {
		try {
			int result = query.deleteById(Role.class, roleId);
			Assert.assertEquals(1, result);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void deleteWhere() {
		try {
			int result = query.delete(Role.class, "where role_id=?",
			        new Object[] { roleId });
			Assert.assertEquals(1, result);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}