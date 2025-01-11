package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;

public class EventJoinServer extends Event {

	private String ip;
	
	public EventJoinServer(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
}
