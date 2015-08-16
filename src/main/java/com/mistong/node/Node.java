package com.mistong.node;

public interface Node {

	public String  getAddress();
	
	public Boolean register();
	
	public Boolean start();
	
	public Boolean stop();
	
	public Boolean canStop();
}
