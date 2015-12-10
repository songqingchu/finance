package com.taobao.learn.collection;

import java.util.TreeSet;

public class TreeSetTest {

	public static void main(String args[]){
		TreeSet<Integer> s=new TreeSet<Integer>();
		s.add(3);
		s.add(2);
		for(Integer i:s){
			System.out.println(i);
		}
	}
}
