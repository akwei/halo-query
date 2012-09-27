package test;

import org.junit.Test;

public class CallerTest {
	@Test
	public void getCaller() {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for (StackTraceElement ste : stack) {
			System.out.println("called by " + ste.getClassName() + "."
					+ ste.getMethodName() + "/" + ste.getFileName());
		}
	}
}