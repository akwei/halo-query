package test.bean;

import halo.query.annotation.Column;
import halo.query.annotation.Table;

/**
 * 一个没有主键的类，有枚举的类
 * Created by akwei on 12/7/14.
 */
@Table(name = "order_item")
public class OrderItem {
    @Column
    private int orderid;
    @Column
    private int itemid;
    @Column
    private OrderItemStatus status;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }
}
