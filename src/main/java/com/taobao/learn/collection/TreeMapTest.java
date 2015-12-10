package com.taobao.learn.collection;

import java.util.Map.Entry;
import java.util.TreeMap;

public class TreeMapTest {

	public static void main(String args[]){
		TreeMap<Integer,Integer> t=new TreeMap<Integer,Integer>(new IntegerComparator());
	    t.put(3, 3);
	    t.put(4, 4);
	    t.put(1, 1);
	    for(Entry<Integer,Integer> e:t.entrySet()){
	    	System.out.println(e.getKey()+":"+e.getValue());
	    }
	}
}

class IntegerComparator implements java.util.Comparator<Integer>{

	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		if(o1>o2){
			return 1;
		}else{
			return -1;
		}
	}
}