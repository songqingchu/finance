package com.taobao.learn.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReadWriteLockTest {

	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	ReadLock readLock = lock.readLock();
	WriteLock writeLock = lock.writeLock();

	public static void main(String args[]) {

	}

	public void add() {
		writeLock.lock();
		try {

		} finally {
			writeLock.unlock();
		}
	}

	public void get() {
		readLock.lock();
		try {

		} finally {
			readLock.unlock();
		}
	}
}
