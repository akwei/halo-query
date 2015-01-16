package halo.query.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParamListUtil {

    private List<Object> list = new ArrayList<Object>();

    public void addBoolean(boolean value) {
        this.list.add(value);
    }

    public void addInt(int value) {
        this.list.add(value);
    }

    public void addObjInt(Integer value) {
        this.list.add(value);
    }

    public void addBigInteger(BigInteger value) {
        this.list.add(value);
    }

    public void addBigDecimal(BigDecimal value) {
        this.list.add(value);
    }

    public void addLong(long value) {
        this.list.add(value);
    }

    public void addObjLong(Long value) {
        this.list.add(value);
    }

    public void addString(String value) {
        this.list.add(value);
    }

    public void addByte(byte value) {
        this.list.add(value);
    }

    public void addObjByte(Byte value) {
        this.list.add(value);
    }

    public void addShort(short value) {
        this.list.add(value);
    }

    public void addObjShort(Short value) {
        this.list.add(value);
    }

    public void addDate(Date value) {
        this.list.add(value);
    }

    public void addChar(char value) {
        this.list.add(value);
    }

    public void addFloat(float value) {
        this.list.add(value);
    }

    public void addObjFloat(Float value) {
        this.list.add(value);
    }

    public void addDouble(double value) {
        this.list.add(value);
    }

    public void addObjDouble(Double value) {
        this.list.add(value);
    }

    public Object[] toObjects() {
        return this.list.toArray(new Object[this.list.size()]);
    }

    public static Object toObject(int value) {
        return value;
    }

    public static Object toObject(Integer value) {
        return value;
    }

    public static Object toObject(long value) {
        return value;
    }

    public static Object toObject(Long value) {
        return value;
    }

    public static Object toObject(short value) {
        return value;
    }

    public static Object toObject(Short value) {
        return value;
    }

    public static Object toObject(byte value) {
        return value;
    }

    public static Object toObject(Byte value) {
        return value;
    }

    public static Object toObject(boolean value) {
        return value;
    }

    public static Object toObject(float value) {
        return value;
    }

    public static Object toObject(Float value) {
        return value;
    }

    public static Object toObject(double value) {
        return value;
    }

    public static Object toObject(Double value) {
        return value;
    }

    public static Object toObject(String value) {
        return value;
    }

    public static Object toObject(BigDecimal value) {
        return value;
    }

    public static Object toObject(BigInteger value) {
        return value;
    }

    public static Object toObject(Date value) {
        return value;
    }

    public static Object toObject(Timestamp value) {
        return value;
    }

    public static Object toObject(java.sql.Date value) {
        return value;
    }

    public static Object toObject(HaloQueryEnum haloQueryEnum) {
        if (haloQueryEnum == null) {
            return null;
        }
        return haloQueryEnum.getValue();
    }
}