package com.taobao.learn.thread.lock;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

	public static ReentrantLock lock = new ReentrantLock();

	public static void main(String args[]) {
		Vector<Integer> obj = new Vector<Integer>();
		Thread consumer = new Thread(new Consumer2(obj, lock));
		Thread producter = new Thread(new Producter2(obj, lock));
		consumer.start();
		producter.start();
	}

}

class Consumer2 implements Runnable {

	private Vector<Integer> obj;
	ReentrantLock lock;

	public Consumer2(Vector<Integer> v, ReentrantLock lock) {
		this.obj = v;
		this.lock = lock;
	}

	public void run() {
		while (true) {
			lock.lock();
			try {
				if (obj.size() == 0) {
					obj.wait();
				}
				for (int i = 0; i < obj.size(); i++) {
					System.out.println("消费: " + obj.get(i));
				}
				obj.clear();
			} catch (Exception e) {

			} finally {
				lock.unlock();
			}
		}
	}
}

class Producter2 implements Runnable {
	private Vector<Integer> obj;
	public ReentrantLock lock;
	public Integer a = 0;

	public Producter2(Vector<Integer> v, ReentrantLock lock) {
		this.obj = v;
		this.lock = lock;
	}

	public void run() {
		while (true) {
			lock.lock();
			try {
				if (obj.size() != 0) {
					obj.wait();
				}
				obj.add(a);
				System.out.println("生产:" + a);
				a++;
 				Thread.sleep(500);
			} catch (Exception e) {

			} finally {
				lock.unlock();
			}
		}
	}
}