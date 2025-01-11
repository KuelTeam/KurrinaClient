package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;

public class EventClickMouse extends Event {

	private int button;
	
	public EventClickMouse(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}
}
