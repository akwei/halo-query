package halo.query.dal;

import java.util.Map;

/**
 * 数据源监听器
 * Created by akwei on 6/14/15.
 */
public interface HaloConnectionListener {

    /**
     * 当获得Connection后触发
     *
     * @param dsKey   数据源key
     * @param dataMap 传递数据的map，如果dataMap没有初始化，就会返回null
     * @throws Exception 发生错误抛出
     */
    void onConnectionOpened(String dsKey, Map<String, Object> dataMap) throws Exception;

    /**
     * 当开启手动提交事务后触发
     *
     * @param dsKey   数据源key
     * @param dataMap 传递数据的map，如果dataMap没有初始化，就会返回null
     * @throws Exception 发生错误抛出
     */
    void onBeginTransaction(String dsKey, Map<String, Object> dataMap) throws Exception;

    /**
     * 当手动提交事务后触发
     *
     * @param dsKey   数据源key
     * @param dataMap 传递数据的map，如果dataMap没有初始化，就会返回null
     * @throws Exception 发生错误抛出
     */
    void onCommit(String dsKey, Map<String, Object> dataMap) throws Exception;

    /**
     * 当事务回滚后触发
     *
     * @param dsKey   数据源key
     * @param dataMap 传递数据的map，如果dataMap没有初始化，就会返回null
     * @throws Exception 发生错误抛出
     */
    void onRollback(String dsKey, Map<String, Object> dataMap) throws Exception;

    /**
     * 当Connection关闭后触发
     *
     * @param dsKey   数据源key
     * @param dataMap 传递数据的map，如果dataMap没有初始化，就会返回null
     * @throws Exception 发生错误抛出
     */
    void onConnectionClosed(String dsKey, Map<String, Object> dataMap) throws Exception;

}
