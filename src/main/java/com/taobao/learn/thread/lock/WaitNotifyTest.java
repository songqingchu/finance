package com.taobao.learn.thread.lock;
import java.util.Vector;

public class WaitNotifyTest {
	
	
	public static void main(String args[]) {
		Vector<Integer> obj = new Vector<Integer>();
		Thread consumer = new Thread(new Consumer(obj));
		Thread producter = new Thread(new Producter(obj));
		consumer.start();
		producter.start();
	}
}


class Consumer implements Runnable {
	
	private Vector<Integer> obj;

	public Consumer(Vector<Integer> v) {
		this.obj = v;
	}

	public void run() {
		synchronized (obj) {
			while (true) {
				try {
					if (obj.size() == 0) {
						obj.wait();
					}
					for(int i=0;i<obj.size();i++){
						System.out.println("消费: " + obj.get(i));
					}
					
					obj.clear();
					obj.notify();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}


class Producter implements Runnable {
	private Vector<Integer> obj;
	public static Integer a=0;
	public Producter(Vector<Integer> v) {
		this.obj = v;
	}

	public void run() {
		synchronized (obj) {
			while (true) {
				try {
					if (obj.size() != 0) {
						obj.wait();
					}

					obj.add(a);
					System.out.println("生产:"+a);
					a++;
					obj.notify();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
