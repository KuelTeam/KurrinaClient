package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;

public class EventRenderSelectedItem extends Event {

	private int color;
	
	public EventRenderSelectedItem(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
}