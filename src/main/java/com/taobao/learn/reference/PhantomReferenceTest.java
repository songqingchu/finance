package com.taobao.learn.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {

	public static void main(String[] args) throws InterruptedException {
		Object obj = new Object();
		ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
		PhantomReference<Object> phanRef = new PhantomReference<Object>(obj,refQueue);
		System.out.println(phanRef.get());
		System.out.println(refQueue.poll());
		obj = null;
		System.gc();
		System.out.println(phanRef.get());
		Reference<? extends Object> r=refQueue.poll();
		System.out.println(r);
	}
}
