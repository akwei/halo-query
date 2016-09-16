package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.bean.MockDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试数据源计数器
 * Created by akwei on 9/16/16.
 */
public class MultiDsTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(200);

    private MockDataSource mockDataSource;

    private int size = 300;

    private int sleep = 0;

    @Before
    public void before() {
        mockDataSource = new MockDataSource();
    }


    @Test
    public void multiGetCon() throws Exception {
        List<Callable<Boolean>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    mockDataSource.getCon();
                    if (sleep > 0) {
                        Thread.sleep(sleep);
                    }
                    mockDataSource.closeCon();
                    return true;
                }
            });
        }
        this.executorService.invokeAll(list);
        Assert.assertEquals("must 0", 0, mockDataSource.getCounterValue());
    }

}
