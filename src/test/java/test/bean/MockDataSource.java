package test.bean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by akwei on 9/16/16.
 */
public class MockDataSource {

    private AtomicInteger counter = new AtomicInteger(0);

    public void getCon() {
        counter.incrementAndGet();
    }

    public void closeCon() {
        counter.decrementAndGet();
    }

    public int getCounterValue() {
        return counter.intValue();
    }
}
