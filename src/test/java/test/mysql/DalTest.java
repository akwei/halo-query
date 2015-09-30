package test.mysql;

import halo.query.Query;
import halo.query.dal.DALInfo;
import halo.query.dal.DALStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test.SuperBaseModelTest;
import test.bean.TbUser;
import test.bean.User;

/**
 * Created by akwei on 9/28/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test3.xml"})
@Transactional
public class DalTest extends SuperBaseModelTest {

    @Autowired
    private Query query;

    /**
     * 由解析器负责进行默认路由，根据userid判断
     */
    @Test
    public void dal() {
        TbUser user0 = new TbUser();
        user0.buildUserId();
        user0.setName("akwei");
        user0.create();
        TbUser user1 = new TbUser();
        user1.buildUserId();
        user1.setName("akwei");
        user1.create();
    }

    /**
     * 测试目的:手动选择数据源，设置错误数据源，不能查询到数据
     *
     * @throws Exception
     */
    @Test
    public void manual0() throws Exception {
        TbUser user0 = new TbUser();
        user0.buildUserId();
        user0.setName("akwei");
        user0.create();
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);//表示手动选择数据源
        if (user0.getUserId() % 2 == 0) {
            dalInfo.setRealTable(TbUser.class, "tb_user_1");
            dalInfo.setDsKey("db1");
        } else {
            dalInfo.setRealTable(TbUser.class, "tb_user_0");
            dalInfo.setDsKey("db0");
        }
        DALStatus.setDalInfo(dalInfo);
        TbUser obj = query.objById(TbUser.class, user0.getUserId());
        Assert.assertNull(obj);
    }

    /**
     * 测试目的:手动选择数据源，设置正确数据源，能查询到数据
     *
     * @throws Exception
     */
    @Test
    public void manual1() throws Exception {
        TbUser user0 = new TbUser();
        user0.buildUserId();
        user0.setName("akwei");
        user0.create();
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);//表示手动选择数据源
        if (user0.getUserId() % 2 == 0) {
            dalInfo.setRealTable(TbUser.class, "tb_user_0");
            dalInfo.setDsKey("db0");
        } else {
            dalInfo.setRealTable(TbUser.class, "tb_user_1");
            dalInfo.setDsKey("db1");
        }
        DALStatus.setDalInfo(dalInfo);
        TbUser obj = query.objById(TbUser.class, user0.getUserId());
        Assert.assertNotNull(obj);
    }

    @Test
    public void example() throws Exception {
        //insert
        TbUser user = new TbUser();
        user.buildUserId();
        user.setName("akwei");
        DALStatus.addParam("userId", user.getUserId());
        query.insertForNumber(user);

        //select
        //查询userId=1的数据，需要设置路由需要的参数
        DALStatus.addParam("userId", user.getUserId());
        TbUser obj = query.objById(TbUser.class, user.getUserId());
        Assert.assertNotNull(obj);

        //手动指定路由位置
        DALInfo dalInfo = new DALInfo();
        dalInfo.setSpecify(true);//表示手动选择数据源
        if (user.getUserId() % 2 == 0) {
            dalInfo.setRealTable(TbUser.class, "tb_user_0");
            dalInfo.setDsKey("db0");
        } else {
            dalInfo.setRealTable(TbUser.class, "tb_user_1");
            dalInfo.setDsKey("db1");
        }
        DALStatus.setDalInfo(dalInfo);//设置指定的路由规则
        obj = query.objById(TbUser.class, user.getUserId());
        Assert.assertNotNull(obj);


        //update / delete
        DALStatus.addParam("userId", user.getUserId());
        user.setName("okok");
        query.update(user);

        DALStatus.addParam("userId", user.getUserId());
        query.deleteById(TbUser.class, new Object[]{user.getUserId()});

        DALStatus.addParam("userId", user.getUserId());
        query.delete(user);
    }
}
