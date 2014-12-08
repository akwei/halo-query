package halo.query.mapping;

/**
 * 提供对枚举的支持，自定义枚举需要实现如下的方法，方法举例
 * <pre>
 * public static UserSex findByValue(int value) {
 *      switch (value) {
 *          case 1:
 *              return MALE;
 *          case 2:
 *              return FEMALE;
 *      }
 *      throw new RuntimeException("not support value" + value);
 * }
 * </pre>
 * <code>
 * <p/>
 * </code>
 * Created by akwei on 12/7/14.
 */
public interface HaloQueryEnum {
    int getValue();
}
