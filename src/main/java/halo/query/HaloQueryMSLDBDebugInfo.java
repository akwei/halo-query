package halo.query;

/**
 * Created by akwei on 7/6/14.
 */
public class HaloQueryMSLDBDebugInfo extends HaloQueryDebugInfo {
    
    private static HaloQueryMSLDBDebugInfo instance = new HaloQueryMSLDBDebugInfo(false);

    public static HaloQueryMSLDBDebugInfo getInstance() {
        return instance;
    }

    public HaloQueryMSLDBDebugInfo(boolean enableDebug) {
        super(enableDebug);
    }
}
