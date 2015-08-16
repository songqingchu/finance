package com.mistong.node.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ControlNodeListner {

	public HashMap<String,Map<String,IoSession>> nodes=new HashMap<String,Map<String,IoSession>>();
	
	public HashSet<IoSession> nodeSet=new HashSet<IoSession>();
	
	public void onMessageCreated(IoSession session){
		nodeSet.add(session);
	}
	
	public void onMessageConnect(IoSession session,Object[] Message){
		
	}
	
	public void onMessageDisconnect(IoSession session,Object Message){
		nodeSet.remove(session);
	}
	
	public void onMessageDestroyed(IoSession session){
		nodeSet.remove(session);
	}
	
	public void onMessageReceived(IoSession session,Object Message){
		
	}
	
	public
}
