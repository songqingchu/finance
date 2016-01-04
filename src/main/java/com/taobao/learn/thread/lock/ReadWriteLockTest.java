package com.taobao.learn.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

	Object data;
	volatile boolean cacheValid;
	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	void processCachedData() {
		rwl.readLock().lock();// @1
		if (!cacheValid) {
			// Must release read lock before acquiring write lock
			rwl.readLock().unlock();// @4
			rwl.writeLock().lock();// @2
			// Recheck state because another thread might have acquired
			// write lock and changed state before we did.
			if (!cacheValid) {// @3
				// data = ...
				cacheValid = true;
			}
			// Downgrade by acquiring read lock before releasing write lock
			rwl.readLock().lock();
			rwl.writeLock().unlock(); // Unlock write, still hold read
		}

		// use(data);
		rwl.readLock().unlock();
	}
}
