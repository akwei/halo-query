package halo.query.dal.slave;

import java.util.List;

/**
 * slave模式数据源选择分析策略
 * Created by akwei on 9/16/16.
 */
public interface SlaveSelectStrategy {

    /**
     * 进行分析选择，获得最终的数据源key
     *
     * @param masterDsKey master数据源key,不会为null
     * @param slaveDsKeys master对应的可用的数据源key，可能为null
     * @return 最终选定的数据源key.返回值为null时，表示无可用slave
     */
    String parse(String masterDsKey, List<String> slaveDsKeys);

}
