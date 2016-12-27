package halo.query;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 将对象转化为json
     *
     * @param obj 对象
     * @return json string
     */
    public static String build(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json转化为 List 或者 Map
     *
     * @param json json
     * @param cls  Map.class or List.class
     * @param <T>  对象泛型
     * @return 解析的对象
     */
    public static <T> Object parse(String json, Class<T> cls) {
        if (json.trim().isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}