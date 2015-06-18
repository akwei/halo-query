package halo.query.dal;

import javax.sql.DataSource;

/**
 * Created by akwei on 6/18/15.
 */
public class HaloDataSourceWrapper {
    private String master;
    private String slave;
    private DataSource dataSource;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
