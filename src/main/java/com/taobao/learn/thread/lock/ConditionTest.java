package com.taobao.learn.thread.lock;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

	ReentrantLock lock = new ReentrantLock();
	Condition empty = lock.newCondition();
	Condition full = lock.newCondition();

	public static void main(String args[]) {

	}

	public static class Consumer implements Runnable {

		private Queue<Integer> q;
		private ReentrantLock lock;
		private Condition empty;
		private Condition full;

		public Consumer(Queue<Integer> v, ReentrantLock lock,Condition empty,Condition full) {
			this.q = v;
			this.lock = lock;
			this.empty=empty;
			this.full=full;
		}

		public void run() {
			while (true) {
				lock.lock();
				try {
					if (q.size() == 0) {
						empty.await();
					}
					System.out.println("消费："+q.poll());
					full.signalAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
				lock.unlock();
			}

		}
	}

	public static class Producer implements Runnable {
		private Queue<Integer> q;
		private ReentrantLock lock;
		private Condition empty;
		private Condition full;
		public static Integer a=0;

		public Producer(Queue<Integer> v, ReentrantLock lock,Condition empty,Condition full) {
			this.q = v;
			this.lock = lock;
			this.empty=empty;
			this.full=full;
		}

		public void run() {
			while (true) {
				lock.lock();
				try {
					if (q.size() == 10) {
						full.await();
					}
                    q.offer(a++);
					empty.signalAll();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				lock.unlock();
			}

		}
	}
}