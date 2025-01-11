package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;

public class EventRenderDamageTint extends Event {

	private float partialTicks;
	
	public EventRenderDamageTint(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
