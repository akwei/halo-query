package halo.datasource;

import javax.sql.DataSource;

/**
 * Created by akwei on 7/13/14.
 */
public abstract class HaloDataSource implements DataSource {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
