package com.taobao.learn.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ConditionTest {

	ReentrantLock lock = new ReentrantLock();
	Condition a=lock.newCondition();
	
	public static void main(String args[]){
		 
	}
	
	public void add(){

     
		lock.lock();  
	    try {  
	        // access the resource protected by this lock  
	    } finally {  
	        // 释放锁  
	        lock.unlock();  
	    } 
	}
}
