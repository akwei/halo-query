package halo.datasource;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 将对象转化为json
     *
     * @param obj 对象
     * @return
     */
    public static String build(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json转化为 List 或者 Map
     *
     * @param json
     * @param cls  Map.class or List.class
     * @return
     */
    public static <T> Object parse(String json, Class<T> cls) {
        if (json.trim().isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, cls);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}