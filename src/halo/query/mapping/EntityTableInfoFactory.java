package halo.query.mapping;

public interface EntityTableInfoFactory {

    EntityTableInfo<?> getEntityTableInfo(Class<?> clazz);
}
