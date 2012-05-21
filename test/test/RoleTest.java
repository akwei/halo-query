package test;

import halo.query.Query;
import halo.query.QueryException;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/query-test.xml" })
@Transactional
public class RoleTest {

    @Resource
    private Query query;

    @Test
    public void insert() {
        Role role = new Role();
        role.setCreateTime(new Date());
        try {
            int roleId = query.insertForNumber(role).intValue();
            System.out.println(roleId);
            role.setRoleId(roleId);
            query.update(role);
        }
        catch (QueryException e) {
            Assert.fail(e.getMessage());
        }
    }
}