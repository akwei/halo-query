package test.mysql;

import halo.query.Query;
import halo.query.dal.DALStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.bean.TbUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-testsimple.xml"})
public class SimpleTest {

    @Autowired
    private Query query;

    /**
     * 事务的slave查询需要走master数据源
     *
     * @throws Exception
     */
    @Test
    public void getFromSlave() throws Exception {
        DALStatus.addParam("userId", 1);
        DALStatus.setSlaveMode();
        query.objById(TbUser.class, 1);
    }

    @Test
    public void sql() throws Exception {
        int size = 1000;
        for (int i = 0; i < size; i++) {
            query.getJdbcSupport().execute("use `querytest`;");
        }

        for (int i = 0; i < size; i++) {
            query.getJdbcSupport().list("select * from `querytest`.user", null, (RowMapper<Object>) (rs, rowNum) -> {
                if (rs.next()) {
                    String string = rs.getString(1);
                    return string;
                }
                return null;
            });
        }

        for (int i = 0; i < size; i++) {
            this.query.getJdbcSupport().execute((ConnectionCallback<Object>) con -> {
                Statement stm = null;
                PreparedStatement pstm = null;
                ResultSet rs = null;
                try {
                    stm = con.createStatement();
                    stm.execute("use `querytest`");
                    pstm = con.prepareStatement("select * from user");
                    rs = pstm.executeQuery();
                    while (rs.next()) {
                        return rs.getString(1);
                    }
                    return null;
                } finally {
                    JdbcUtils.closeStatement(stm);
                    JdbcUtils.closeStatement(pstm);
                    JdbcUtils.closeResultSet(rs);
                }
            });
        }
        /// test

        {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                query.getJdbcSupport().execute("use `querytest`;");
            }
            System.out.println("use : " + (System.currentTimeMillis() - begin));
        }

        {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                this.query.getJdbcSupport().execute((ConnectionCallback<Object>) con -> {
                    Statement stm = null;
                    PreparedStatement pstm = null;
                    ResultSet rs = null;
                    try {
//                        stm = con.createStatement();
//                        stm.execute("use `querytest`");
                        pstm = con.prepareStatement("select * from user");
                        rs = pstm.executeQuery();
                        while (rs.next()) {
                            return rs.getString(1);
                        }
                        return null;
                    } finally {
                        JdbcUtils.closeStatement(stm);
                        JdbcUtils.closeStatement(pstm);
                        JdbcUtils.closeResultSet(rs);
                    }
                });
            }
            System.out.println("list2 : " + (System.currentTimeMillis() - begin));
        }

        {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                query.getJdbcSupport().list("select * from `querytest`.user", null, (RowMapper<Object>) (rs, rowNum) -> {
                    if (rs.next()) {
                        String string = rs.getString(1);
                        return string;
                    }
                    return null;
                });
            }
            System.out.println("list : " + (System.currentTimeMillis() - begin));
        }


    }
}
