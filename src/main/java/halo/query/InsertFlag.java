package halo.query;

/**
 * Created by akwei on 9/29/15.
 */
public enum InsertFlag {
    INSERT_INTO(0),
    REPLACE_INTO(1),
    INSERT_IGNORE_INTO(2),
    INSERT_INTO_ON_DUPLICATE_KEY_UPDATE(3);

    private final int value;

    private InsertFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static InsertFlag findByValue(int value) {
        switch (value) {
            case 0:
                return INSERT_INTO;
            case 1:
                return REPLACE_INTO;
            case 2:
                return INSERT_IGNORE_INTO;
            case 3:
                return INSERT_INTO_ON_DUPLICATE_KEY_UPDATE;
            default:
                return null;
        }
    }
}
