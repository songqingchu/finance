package com.mistong.node.impl;

import com.mistong.node.Node;

public class BaseNode implements Node{
	
	private String ip;

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return ip;
	}

	@Override
	public Boolean register() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean start() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean canStop() {
		// TODO Auto-generated method stub
		return null;
	}

}
