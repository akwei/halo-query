package halo.query.idtool;

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

public class DefIdGeneratorImpl implements IdGenerator {

	public long nextKey(
	        DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer) {
		return dataFieldMaxValueIncrementer.nextLongValue();
	}
}