package halo.query.idtool;

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

public interface IdGenerator {

	public long nextKey(
	        DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer);

	public long nextKeyNeedNewCon(
	        DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer);
}