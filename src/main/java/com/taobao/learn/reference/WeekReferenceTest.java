package com.taobao.learn.reference;

import java.util.Map;
import java.util.WeakHashMap;

public class WeekReferenceTest {

	public static void main(String[] args) {
		Map<Integer, String> map = new WeakHashMap<Integer, String>();
		Integer key = new Integer(1);
		map.put(key, "test");
		// key不再有强引用
		key = null;
		System.gc();
		// 等待一段时间，进行垃圾回收
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(map.size());
	}
}
