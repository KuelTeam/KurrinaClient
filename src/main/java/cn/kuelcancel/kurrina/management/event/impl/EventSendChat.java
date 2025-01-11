package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;

public class EventSendChat extends Event {

	private String message;
	
	public EventSendChat(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
