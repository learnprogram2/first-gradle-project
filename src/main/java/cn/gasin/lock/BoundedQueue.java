package cn.gasin.lock;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有界队列, 生产者生产满了, 就停下; 消费者消费完了, 就停下
 * @author Wangyk
 * @date 2019/7/18 21:55
 */
public class BoundedQueue<T> {

	private Object[] items;


	private int addIndex, removeIndex, count;
	private Lock lock = new ReentrantLock();

	private Condition empty = lock.newCondition();
	private Condition full  = lock.newCondition();


	public BoundedQueue(int size) {
		items = new Object[size];
	}

	//t添加一个元素, 如果满了就await
	public void add(Object o) {
		lock.lock();
		try {
			//如果满了,就await
			while (count == items.length) {
				full.await();
			}

			items[addIndex] = o;
			addIndex++;
			if (addIndex == items.length) {
				addIndex = 0;
			}
			++count;
			//把因为空而await的线程唤醒
			empty.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	//从头部移出一个元素
	public Object remove() {
		lock.lock();
		try {
			while (count == 0) {
				empty.await();
			}

			Object item = items[removeIndex];
			if (++removeIndex == items.length) {
				removeIndex = 0;
			}
			--count;
			//把因为满了的线程唤醒
			full.signal();

			return item;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return null;
	}

}
