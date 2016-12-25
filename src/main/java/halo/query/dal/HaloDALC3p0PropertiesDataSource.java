package halo.query.dal;

/**
 * 通过properties文件创建c3p0 dal使用的数据源
 * Created by akwei on 7/13/14.
 */
public class HaloDALC3p0PropertiesDataSource extends HaloPropertiesDataSource {

    public static final String NAME = "com.mchange.v2.c3p0.ComboPooledDataSource";

    public HaloDALC3p0PropertiesDataSource() {
        this.setDataSourceClassName(NAME);
    }
}
