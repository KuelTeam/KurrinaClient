package cn.kuelcancel.kurrina.management.security;

import cn.kuelcancel.kurrina.Kurrina;

public class SecurityFeature {

	public SecurityFeature() {
		Kurrina.getInstance().getEventManager().register(this);
	}
}
