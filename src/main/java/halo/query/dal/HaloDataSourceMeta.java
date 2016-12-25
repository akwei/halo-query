package halo.query.dal;

/**
 * 暂时没想好
 * Created by akwei on 9/17/16.
 */
public class HaloDataSourceMeta {

    private boolean discarded;

    private String dsKey;

    public String getDsKey() {
        return dsKey;
    }

    public void setDsKey(String dsKey) {
        this.dsKey = dsKey;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

}
