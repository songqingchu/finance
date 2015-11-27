package com.taobao.learn.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 *  如果我们只需要某个类里的某个字段，那么就需要使用原子更新字段类，Atomic包提供了以下三个类：
    AtomicIntegerFieldUpdater：   原子更新整型的字段的更新器。
    AtomicLongFieldUpdater：            原子更新长整型字段的更新器。
    AtomicStampedReference：            原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更数据和数据的版本号，可以解决使用CAS进行原子更新时，可能出现的ABA问题。
            原子更新字段类都是抽象类，每次使用都时候必须使用静态方法newUpdater创建一个更新器。原子更新类的字段的必须使用public volatile修饰符。
 */
public class AtomicIntegerFieldUpdaterTest {

	private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");
	private static AtomicLongFieldUpdater<User> b = AtomicLongFieldUpdater.newUpdater(User.class, "old2");

	public static void main(String[] args) {
		User conan = new User("conan", 10 , 20);
		System.out.println(a.getAndIncrement(conan));
		System.out.println(b.incrementAndGet(conan));
	}

	
	
	public static class User {
		private String name;
		public volatile int old;
		public volatile int old2;
		
		public User(String name, int old,int old2) {
			this.name = name;
			this.old = old;
			this.old2=old2;
		}
		public String getName() {
			return name;
		}
		public int getOld() {
			return old;
		}
		public int getOld2() {
			return old2;
		}
		public void setOld2(int old2) {
			this.old2 = old2;
		}
	}
}