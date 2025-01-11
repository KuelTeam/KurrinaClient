package cn.kuelcancel.kurrina.management.notification;

import cn.kuelcancel.kurrina.management.nanovg.font.Icon;

public enum NotificationType {
	INFO(Icon.INFO), 
	WARNING(Icon.ALERT_TRIANGLE), 
	ERROR(Icon.X_CIRCLE),
	SUCCESS(Icon.CHECK),
	MUSIC(Icon.MUSIC);
	
	private String icon;
	
	private NotificationType(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
}
