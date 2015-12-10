package com.taobao.learn.thread.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 *AtomicMarkableReference和AtomicStampedReference功能差不多，有点区别的是：它描述更加简单的是与否的关系，通常ABA问题只有两种状态，
 *而AtomicStampedReference是多种状态，那么为什么还要有AtomicMarkableReference呢，因为它在处理是与否上面更加具有可读性，
 *而AtomicStampedReference过于随意定义状态，并不便于阅读大量的是和否的关系，它可以被认为是一个计数器或状态列表等信息，java提倡通过类名知道其意义，
 *所以这个类的存在也是必要的。
 * @author Auser
 *
 */
public class AtomicMarkableReferenceTest {
public final static AtomicMarkableReference <String> ATOMIC_REFERENCE = new AtomicMarkableReference<String>("abc" , false);  
    
    public static void main(String []args) {  
        for(int i = 0 ; i < 100 ; i++) {  
            final int num = i;  
            final boolean isMarked = ATOMIC_REFERENCE.isMarked();  
            new Thread() {  
                public void run() {  
                    try {  
                        Thread.sleep(Math.abs((int)(Math.random() * 100)));  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    if(ATOMIC_REFERENCE.compareAndSet("abc" , "abc2" , isMarked , true)) {  
                        System.out.println("我是线程：" + num + ",我获得了锁进行了对象修改！");  
                    }  
                }  
            }.start();  
        }  
 
    }  
}  
