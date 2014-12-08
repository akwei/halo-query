package test.bean;

import halo.query.mapping.HaloQueryEnum;

/**
 * Created by akwei on 12/7/14.
 */
public enum UserSex implements HaloQueryEnum {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);
    private int value;

    @Override
    public int getValue() {
        return this.value;
    }

    UserSex(int value) {
        this.value = value;
    }

    public static UserSex findByValue(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            default:
                throw new RuntimeException("not support value " + value);
        }
    }
}
