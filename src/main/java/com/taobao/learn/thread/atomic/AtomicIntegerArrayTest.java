package com.taobao.learn.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 通过原子的方式更新数组里的某个元素，Atomic包提供了以下三个类： 
 * AtomicIntegerArray：                                                                                                      原子更新整型数组里的元素。
 * AtomicLongArray：                                                                                                               原子更新长整型数组里的元素。 
 * AtomicReferenceArray：                                                                                                原子更新引用类型数组里的元素。
 * 
 * AtomicIntegerArray类主要是提供原子的方式更新数组里的整型，其常用方法如下 
 * int addAndGet(int i, intdelta)：                                                                   以原子方式将输入值与数组中索引i的元素相加。 
 * boolean compareAndSet(int i, int expect, int update)： 如果当前值等于预期值，则以原子方式将数组位置i的元素设置成update值。
 */

public class AtomicIntegerArrayTest {
	

	public static void main(String[] args) {
		testAtomicReferenceArray();
	}
	
	public static void testAtomicReferenceArray(){
		String[] a=new String[]{"1","2"};
		AtomicReferenceArray<String> aa=new AtomicReferenceArray<String>(a);
		aa.compareAndSet(0, "1", "3");
		System.out.println(a[0]);
		System.out.println(aa.get(0));
	}
	
	public static void testAtomicIntegerArray(){
	    int[] value = new int[] { 1, 2 };
	    AtomicIntegerArray ai = new AtomicIntegerArray(value);
		ai.getAndSet(0, 3);
		System.out.println(ai.get(0));
		System.out.println(value[0]);
	}
}