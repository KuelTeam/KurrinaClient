package cn.kuelcancel.kurrina.management.notification;

import java.util.concurrent.LinkedBlockingQueue;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.language.TranslateText;

public class NotificationManager {

	private LinkedBlockingQueue<Notification> notifications = new LinkedBlockingQueue<Notification>();
	
	public NotificationManager() {
		Kurrina.getInstance().getEventManager().register(new NotificationHandler(notifications));
	}
	
	public void post(TranslateText title, TranslateText message, NotificationType type) {
		notifications.add(new Notification(title, message, type));
	}
}
