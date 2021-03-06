package test.bean;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class HaloConnectionCustomizer extends AbstractConnectionCustomizer {

    private static Logger logger = LoggerFactory.getLogger(HaloConnectionCustomizer.class);

    @Override
    public void onAcquire(Connection c, String parentDataSourceIdentityToken) throws Exception {
        logger.info("acquire con from " + parentDataSourceIdentityToken);
    }

    @Override
    public void onDestroy(Connection c, String parentDataSourceIdentityToken) throws Exception {
        logger.info("destory con from " + parentDataSourceIdentityToken);
    }

    @Override
    public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {
        logger.info("checkout con from " + parentDataSourceIdentityToken);
    }

    @Override
    public void onCheckIn(Connection c, String parentDataSourceIdentityToken) throws Exception {
        logger.info("checkin con from " + parentDataSourceIdentityToken);
    }
}
