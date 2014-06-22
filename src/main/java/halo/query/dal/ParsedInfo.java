package halo.query.dal;

public class ParsedInfo {

	private String dsKey;

	private String realTableName;

	public ParsedInfo() {
	}

	public ParsedInfo(String dsKey, String realTableName) {
		this.dsKey = dsKey;
		this.realTableName = realTableName;
	}

	public String getDsKey() {
		return dsKey;
	}

	public void setDsKey(String dsKey) {
		this.dsKey = dsKey;
	}

	public String getRealTableName() {
		return realTableName;
	}

	public void setRealTableName(String realTableName) {
		this.realTableName = realTableName;
	}
}