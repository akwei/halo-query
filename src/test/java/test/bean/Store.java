package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

/**
 * Created by akwei on 9/2/14.
 */
@Table(name = "store")
public class Store extends BaseModel {

    @Id(1)
    @Column("merchant_id")
    private int merchantId;

    @Id(0)
    @Column("store_id")
    private int storeId;

    @Column("create_time")
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }
}
