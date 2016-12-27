package halo.query.dal;

/**
 * 数据源状态
 * Created by akwei on 9/17/16.
 */
public enum HaloDataSourceStatus {
    /**
     * 存活的
     */
    ALIVE(1),
    /**
     * 被废弃的，等待被清除
     */
    DISCARDED(2);

    HaloDataSourceStatus(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
