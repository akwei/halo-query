package test.mysql;

import halo.query.Query;
import halo.query.dal.DALInfo;
import halo.query.dal.DALStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.bean.TbUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-testsimple.xml"})
public class SimpleTest {

    @Autowired
    private Query query;

    /**
     * 事务的slave查询需要走master数据源
     */
    @Test
    public void getFromSlave0() throws Exception {
        DALStatus.addParam("userId", 1);
        DALStatus.setSlaveMode();
        query.objById(TbUser.class, 1);
    }

    @Test
    public void sql() throws Exception {
        //进行分表分库读取
        DALStatus.addParam("userId", 1);
        query.objById(TbUser.class, 1);

        DALStatus.addParam("userId", 2);
        query.objById(TbUser.class, 2);


        //测试从库读取
        DALStatus.addParam("userId", 1);
        DALStatus.setSlaveMode();
        query.objById(TbUser.class, 1);

        DALStatus.addParam("userId", 2);
        DALStatus.setSlaveMode();
        query.objById(TbUser.class, 2);
    }

    /**
     * 测试只有slave数据源的设置
     */
    @Test
    public void getFromSlave1() throws Exception {
        _getFromSlave();
    }

    @Test
    public void getFromSlave2() throws Exception {
        int size = 100;
        for (int i = 0; i < size; i++) {
            _getFromSlave();
        }
    }

    private void _getFromSlave() {
        DALInfo dalInfo = DALInfo.createForManual();
        dalInfo.setDsKey("db2");
        dalInfo.setRealTable(TbUser.class, "tb_user_1");
        DALStatus.setDalInfo(dalInfo);
        query.objById(TbUser.class, 1);
    }

//    @Test
//    public void sql1() throws Exception {
//        int size = 1000;
//        for (int i = 0; i < size; i++) {
//            query.getJdbcSupport().execute("use `querytest`;");
//        }
//
//        for (int i = 0; i < size; i++) {
//            query.getJdbcSupport().list("select * from `querytest`.user", null, (RowMapper<Object>) (rs, rowNum) -> {
//                if (rs.next()) {
//                    String string = rs.getString(1);
//                    return string;
//                }
//                return null;
//            });
//        }
//
//        for (int i = 0; i < size; i++) {
//            this.query.getJdbcSupport().execute((ConnectionCallback<Object>) con -> {
//                Statement stm = null;
//                PreparedStatement pstm = null;
//                ResultSet rs = null;
//                try {
//                    stm = con.createStatement();
//                    stm.execute("use `querytest`");
//                    pstm = con.prepareStatement("select * from user");
//                    rs = pstm.executeQuery();
//                    while (rs.next()) {
//                        return rs.getString(1);
//                    }
//                    return null;
//                } finally {
//                    JdbcUtils.closeStatement(stm);
//                    JdbcUtils.closeStatement(pstm);
//                    JdbcUtils.closeResultSet(rs);
//                }
//            });
//        }
//        /// test
//
//        {
//            long begin = System.currentTimeMillis();
//            for (int i = 0; i < size; i++) {
//                query.getJdbcSupport().execute("use `querytest`;");
//            }
//            System.out.println("use : " + (System.currentTimeMillis() - begin));
//        }
//
//        {
//            long begin = System.currentTimeMillis();
//            for (int i = 0; i < size; i++) {
//                this.query.getJdbcSupport().execute((ConnectionCallback<Object>) con -> {
//                    Statement stm = null;
//                    PreparedStatement pstm = null;
//                    ResultSet rs = null;
//                    try {
////                        stm = con.createStatement();
////                        stm.execute("use `querytest`");
//                        pstm = con.prepareStatement("select * from user");
//                        rs = pstm.executeQuery();
//                        while (rs.next()) {
//                            return rs.getString(1);
//                        }
//                        return null;
//                    } finally {
//                        JdbcUtils.closeStatement(stm);
//                        JdbcUtils.closeStatement(pstm);
//                        JdbcUtils.closeResultSet(rs);
//                    }
//                });
//            }
//            System.out.println("list2 : " + (System.currentTimeMillis() - begin));
//        }
//
//        {
//            long begin = System.currentTimeMillis();
//            for (int i = 0; i < size; i++) {
//                query.getJdbcSupport().list("select * from `querytest`.user", null, (RowMapper<Object>) (rs, rowNum) -> {
//                    if (rs.next()) {
//                        String string = rs.getString(1);
//                        return string;
//                    }
//                    return null;
//                });
//            }
//            System.out.println("list : " + (System.currentTimeMillis() - begin));
//        }
//    }
}
