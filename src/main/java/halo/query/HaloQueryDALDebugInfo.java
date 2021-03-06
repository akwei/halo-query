package halo.query;

/**
 * Created by akwei on 7/6/14.
 */
public class HaloQueryDALDebugInfo extends HaloQueryDebugInfo {

    private static HaloQueryDALDebugInfo instance = new HaloQueryDALDebugInfo(false);

    public static HaloQueryDALDebugInfo getInstance() {
        return instance;
    }

    public HaloQueryDALDebugInfo() {

    }

    public HaloQueryDALDebugInfo(boolean enableDebug) {
        super(enableDebug);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
