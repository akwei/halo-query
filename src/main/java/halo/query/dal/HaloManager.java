package halo.query.dal;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源管理
 * Created by akwei on 9/16/16.
 */
public class HaloManager {

    private HaloDALDataSource haloDALDataSource;

    public void setHaloDALDataSource(HaloDALDataSource haloDALDataSource) {
        this.haloDALDataSource = haloDALDataSource;
    }

    /**
     * 获取从库数据源状态
     *
     * @param status 数据源状态
     * @return list
     */
    public List<HaloDataSourceMeta> getSlaveDataSourceMeta(HaloDataSourceStatus status) {
        List<HaloDataSourceWrapper> dataSources = this.haloDALDataSource.getDataSources();
        List<HaloDataSourceMeta> metas = new ArrayList<>();
        for (HaloDataSourceWrapper dataSource : dataSources) {
            HaloDataSourceMeta obj = new HaloDataSourceMeta();
//            obj.setDiscarded(dataSource.isDiscarded());
            obj.setDsKey(dataSource.getDsKey());
            metas.add(obj);
        }
        if (status == null) {
            return metas;
        }
        if (status.equals(HaloDataSourceStatus.ALIVE)) {
            List<HaloDataSourceMeta> list = new ArrayList<>();
            for (HaloDataSourceMeta obj : metas) {
                if (obj.isDiscarded()) {
                    continue;
                }
                list.add(obj);
            }
            return list;
        }
        List<HaloDataSourceMeta> list = new ArrayList<>();
        for (HaloDataSourceMeta obj : metas) {
            if (!obj.isDiscarded()) {
                continue;
            }
            list.add(obj);
        }
        return list;
    }
}
