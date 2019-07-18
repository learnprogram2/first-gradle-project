package cn.gasin.lock.condition;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Wangyk
 * @date 2019/7/18 21:20
 */
public class ConditionTest {

	private ReentrantLock lock      = new ReentrantLock();
	private Condition     condition = lock.newCondition();


	@Test
	public void testCondition() throws Exception {

		conditionWait();

		conditionSignal();
	}

	private void conditionWait() {
		lock.lock();
		try {
			condition.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void conditionSignal() {
		lock.lock();
		try {
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

}
