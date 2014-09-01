package halo.query.mapping;

/**
 * 此接口的存在是为了利用字节码进行类生成，提高性能，避免使用反射操作
 *
 * @param <T>
 * @author akwei
 */
public interface SQLMapper<T> {

    /**
     * 返回insert需要的参数
     *
     * @param t               需要insert的对象
     * @param hasIdFieldValue 是否包含id的值，对于联合主键，此参数无效
     * @return
     */
    Object[] getParamsForInsert(T t, boolean hasIdFieldValue);

    /**
     * 返回update需要的参数以及id所对应的参数组成的数组
     *
     * @param t 需要update的对象
     * @return
     */
    Object[] getParamsForUpdate(T t);

    /**
     * 获得对象主键值，数组中的顺序为@Id标识对应的顺序
     *
     * @param t 对象
     * @return
     */
    Object[] getIdParams(T t);

}
