package halo.query.dal;

/**
 * 数据库连接事件监听
 * Created by akwei on 4/11/16.
 */
public interface DALConnectionListener {

    void onDALOpened();

    void onDALClosed();

    void onDALBeganTranscation();

    void onDALRollbacked();

    void onDALCommited();

}
