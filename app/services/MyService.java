package services;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

/**
 * @author tuyenng
 *
 */
@Singleton
public class MyService implements MyInstance {
	private final AtomicInteger atomicCounter = new AtomicInteger();

	@Override
	public int nextCount() {
		return atomicCounter.getAndAdd(2);
	}
}
