package ds;

import halo.query.mslb.MSLBC3p0PropertiesDataSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by akwei on 7/12/14.
 */
public class DataSourceTest {
    @Test
    public void createDs() {
        MSLBC3p0PropertiesDataSource ds = new MSLBC3p0PropertiesDataSource();
        ds.setName("c3p0");
        try {
            ds.afterPropertiesSet();
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
