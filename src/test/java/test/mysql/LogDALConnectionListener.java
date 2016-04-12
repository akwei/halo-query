package test.mysql;

import halo.query.dal.BaseDALConnectionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * for test
 * Created by akwei on 4/11/16.
 */
public class LogDALConnectionListener extends BaseDALConnectionListener {

    private static Log logger = LogFactory.getLog(LogDALConnectionListener.class);

    @Override
    public void onDALOpened() {
        logger.info("onDALOpened()");
    }

    @Override
    public void onDALClosed() {
        logger.info("onDALClosed()");
    }

    @Override
    public void onDALRollbacked() {
        logger.info("onDALRollbacked()");
    }

    @Override
    public void onDALBeganTranscation() {
        logger.info("onDALBeganTranscation()");
    }

    @Override
    public void onDALCommited() {
        logger.info("onDALCommited()");
    }
}
