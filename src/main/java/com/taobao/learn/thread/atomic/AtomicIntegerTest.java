package com.taobao.learn.thread.atomic;
import java.util.concurrent.atomic.AtomicInteger;



public class AtomicIntegerTest {

	static AtomicInteger ai = new AtomicInteger(1);
	public static void main(String[] args) {
		System.out.println(ai.getAndIncrement());
		System.out.println(ai.get());
	}
}