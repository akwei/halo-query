package halo.query;

import org.springframework.beans.factory.InitializingBean;

/**
 * halo配置
 * Created by akwei on 9/2/16.
 */
public class HaloConfig implements InitializingBean {

    private static HaloConfig instance = new HaloConfig();

    private int logSlowConMillis;

    public static HaloConfig getInstance() {
        return instance;
    }

    /**
     * 设置记录获取连接慢日志的超时时间，时间&lt;=0时不记录日志。
     *
     * @param logSlowConMillis 单位:毫秒
     */
    public void setLogSlowConMillis(int logSlowConMillis) {
        this.logSlowConMillis = logSlowConMillis;
    }

    public boolean isSlowCon(int time) {
        if (this.logSlowConMillis <= 0) {
            return false;
        }
        if (time >= this.logSlowConMillis) {
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HaloConfig.instance = this;
    }
}
