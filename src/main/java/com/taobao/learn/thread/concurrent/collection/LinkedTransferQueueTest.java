package com.taobao.learn.thread.concurrent.collection;


/**
 * 基于链表的无界队列。除了通常的队列操作，它还有一系列的transfer方法，可以让生产者直接给等待的消费者传递信息，这样就不用将元素存储到队列中了。
 * 这是一个基于CAS操作的无锁集合
 * @author Auser
 *
 */
public class LinkedTransferQueueTest {

}
