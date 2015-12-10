package com.taobao.learn.thread.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子更新基本类型的AtomicInteger，只能更新一个变量，如果要原子的更新多个变量，就需要使用这个原子更新引用类型提供的类。Atomic包提供了以下三个类：
 * AtomicReference：                                              原子更新引用类型。
 * AtomicReferenceFieldUpdater：          原子更新引用类型里的字段。 
 * AtomicMarkableReference：                      原子更新带有标记位的引用类型。可以原子的更新一个布尔类型的标记位和引用类型。
 * 构造方法是AtomicMarkableReference(V initialRef, boolean initialMark)
 */
public class AtomicReferenceTest {

	public static AtomicReference<User> atomicUserRef = new AtomicReference<User>();

	public static void main(String[] args) {
		User user = new User("conan", 15);
		atomicUserRef.set(user);
		User updateUser = new User("Shinichi", 17);
		atomicUserRef.compareAndSet(user, updateUser);
		System.out.println(atomicUserRef.get().getName());
		System.out.println(atomicUserRef.get().getOld());
	}
	
	
	public void test2(){
		  final AtomicReference <String> ATOMIC_REFERENCE = new AtomicReference<String>("abc");  
	      
		        for(int i = 0 ; i < 100 ; i++) {  
		            final int num = i;  
		            new Thread() {  
		                public void run() {  
		                    try {  
		                        Thread.sleep(Math.abs((int)(Math.random() * 100)));  
		                    } catch (InterruptedException e) {  
		                        e.printStackTrace();  
		                    }  
		                    if(ATOMIC_REFERENCE.compareAndSet("abc", new String("abc"))) {  
		                        System.out.println("我是线程：" + num + ",我获得了锁进行了对象修改！");  
		                    }  
		                }  
		            }.start();  
		        }  
		     
	}

	public static class User {
		private String name;
		private int old;

		public User(String name, int old) {
			this.name = name;
			this.old = old;
		}

		public String getName() {
			return name;
		}

		public int getOld() {
			return old;
		}
	}
}