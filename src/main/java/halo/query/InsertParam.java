package halo.query;

import java.util.List;

public class InsertParam<T> {

    private T object;

    private List<T> objects;

    private InsertFlag insertFlag;

    private String[] updateCols;

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public InsertFlag getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(InsertFlag insertFlag) {
        this.insertFlag = insertFlag;
    }

    public String[] getUpdateCols() {
        return updateCols;
    }

    public void setUpdateCols(String[] updateCols) {
        this.updateCols = updateCols;
    }

    public static <T> InsertParam<T> create() {
        return new InsertParam<>();
    }
}
