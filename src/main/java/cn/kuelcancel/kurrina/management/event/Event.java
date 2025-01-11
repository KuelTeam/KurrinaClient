package cn.kuelcancel.kurrina.management.event;

import java.lang.reflect.InvocationTargetException;

import cn.kuelcancel.kurrina.Kurrina;

public abstract class Event {

	private boolean cancelled;

	public Event call() {
		this.cancelled = false;
		Event.call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	private static void call(Event event) {
		
		Kurrina instance = Kurrina.getInstance();
		EventManager eventManager = instance.getEventManager();
		ArrayHelper<Data> dataList = eventManager.get(event.getClass());
		
		if (dataList != null) {
			for (Data data : dataList) {
				try {
					data.target.invoke(data.source, event);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
