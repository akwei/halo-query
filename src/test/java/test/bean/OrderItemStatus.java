package test.bean;

import halo.query.mapping.HaloQueryEnum;

/**
 * Created by akwei on 12/7/14.
 */
public enum OrderItemStatus implements HaloQueryEnum {
    NO(0),
    YES(1);
    private int value;

    public int getValue() {
        return value;
    }

    OrderItemStatus(int value) {
        this.value = value;
    }

    public static OrderItemStatus findByValue(int value) {
        switch (value) {
            case 0:
                return NO;
            case 1:
                return YES;
            default:
                return NO;
        }
    }
}
