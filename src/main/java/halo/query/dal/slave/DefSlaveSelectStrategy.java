package halo.query.dal.slave;

import halo.query.HaloQueryMSLDBDebugInfo;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * 默认的slave选取策略
 * Created by akwei on 9/16/16.
 */
public class DefSlaveSelectStrategy implements SlaveSelectStrategy {

    private static Logger logger = Logger.getLogger(DefSlaveSelectStrategy.class);

    @Override
    public String parse(String masterDsKey, List<String> slaveDsKeys) {
        if (slaveDsKeys == null || slaveDsKeys.isEmpty()) {
            return null;
        }
        if (slaveDsKeys.size() == 1) {
            String dsKey = slaveDsKeys.get(0);
            if (HaloQueryMSLDBDebugInfo.getInstance().isEnableDebug()) {
                logger.info("will return slave datasource [" + dsKey + "] for only one slave");
            }
            return dsKey;
        }
        Random random = new Random();
        int index = random.nextInt(slaveDsKeys.size());
        String dsKey = slaveDsKeys.get(index);
        if (HaloQueryMSLDBDebugInfo.getInstance().isEnableDebug()) {
            logger.info("will return slave datasource [" + dsKey + "]");
        }
        return dsKey;
    }
}
