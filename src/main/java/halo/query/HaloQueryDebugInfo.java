package halo.query;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by akwei on 7/6/14.
 */
public class HaloQueryDebugInfo implements InitializingBean {

    private static HaloQueryDebugInfo haloQueryDebugInfo = new HaloQueryDebugInfo(false);

    private boolean enableDebug;

    public static HaloQueryDebugInfo getInstance() {
        return haloQueryDebugInfo;
    }

    public HaloQueryDebugInfo(boolean enableDebug) {
        this.setEnableDebug(enableDebug);
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        haloQueryDebugInfo = this;
    }
}
