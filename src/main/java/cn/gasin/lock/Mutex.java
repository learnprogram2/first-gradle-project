package cn.gasin.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Wangyk
 * @date 2019/7/15 23:56
 * <p>
 * 独占锁
 */
public class Mutex implements Lock {

	//1. 静态内部类
	private static class Sync extends AbstractQueuedSynchronizer {
		//是否处于独占状态
		@Override
		protected boolean isHeldExclusively() {
			return getState() == 1;
		}

		//当前状态为0时候获取锁
		@Override
		protected boolean tryAcquire(int arg) {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}

		//释放锁, 设置状态为0
		@Override
		protected boolean tryRelease(int arg) {
			if (getState() == 0)
				throw new IllegalMonitorStateException("状态不为0");
			setExclusiveOwnerThread(null);
			setState(0);
			return true;
		}

		//返回一个condition, 每一个condition包含一个condition队列
		Condition newCondition() {
			return new ConditionObject();
		}
	}

	//队列同步器
	private final Sync sync = new Sync();

	@Override
	public void lock() {
		sync.acquire(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

  	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}
}
