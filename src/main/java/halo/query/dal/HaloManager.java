package halo.query.dal;

import java.util.List;

/**
 * 资源管理的入口
 * Created by akwei on 9/16/16.
 */
public class HaloManager {

    private HaloDALDataSource haloDALDataSource;

    public HaloDALDataSource getHaloDALDataSource() {
        return haloDALDataSource;
    }

    public void setHaloDALDataSource(HaloDALDataSource haloDALDataSource) {
        this.haloDALDataSource = haloDALDataSource;
    }

    /**
     * 获取从库数据源列表
     *
     * @param status 数据源状态
     * @return list
     */
    public List<HaloDataSourceWrapper> getSlaveDataSources(HaloDataSourceStatus status) {
        return null;
    }


}
