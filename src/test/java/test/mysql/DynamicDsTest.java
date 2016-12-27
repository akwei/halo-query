package test.mysql;

import halo.query.Query;
import halo.query.dal.DALStatus;
import halo.query.dal.HaloDALDataSource;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.bean.TbUser;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源测试
 * Created by akwei on 9/18/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-test4.xml"})
//@Transactional
public class DynamicDsTest {

    ObjectMapper mapper = new ObjectMapper();

    @Resource
    private HaloDALDataSource haloDALDataSource;

    @Resource
    private Query query;

    /**
     * 测试添加db1的slave数据源 db5_slave
     *
     * @throws Exception
     */
    @Test
    public void t_001_addDs() throws Exception {
        String db5SlaveStr = "{\"url\":\"127.0.0.1:3306/db5_slave\"}";
        String db5 = "db1_slave5";
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("dsKey", db5);
        ctxMap.put("cfgMap", mapper.readValue(db5SlaveStr, Map.class));

        List<String> list = this.haloDALDataSource.getSlaveDsKeys("db1");
        int size = list.size();
        String last = list.get(list.size() - 1);
        this.haloDALDataSource.loadDataSource(ctxMap, "db1");

        {
            List<String> list0 = this.haloDALDataSource.getSlaveDsKeys("db1");
            Assert.assertEquals(size + 1, list0.size());
            Assert.assertEquals(db5, list0.get(list0.size() - 1));
        }

        //remove db5
        this.haloDALDataSource.removeDataSource(db5);
        {
            List<String> list0 = this.haloDALDataSource.getSlaveDsKeys("db1");
            Assert.assertEquals(size, list0.size());
            Assert.assertEquals(last, list0.get(list0.size() - 1));
        }

        size = 0;
        this.haloDALDataSource.loadDataSource(ctxMap, "db0");
        {
            List<String> list0 = this.haloDALDataSource.getSlaveDsKeys("db0");
            Assert.assertEquals(size + 1, list0.size());
            Assert.assertEquals(db5, list0.get(list0.size() - 1));
        }

    }

    @Test
    public void t_002_reduce() throws Exception {

        String db1slave = "db1_slave1";
        List<String> list = this.haloDALDataSource.getSlaveDsKeys("db1");
        Assert.assertEquals(1, list.size());

        DALStatus.setSlaveMode();
        DALStatus.addParam("userId", 1);
        query.objById(TbUser.class, 1);

        this.haloDALDataSource.removeDataSource(db1slave);
        System.out.println("remove ok");

        DALStatus.setSlaveMode();
        DALStatus.addParam("userId", 1);
        query.objById(TbUser.class, 1);

        Thread.sleep(5000);
    }

}
