package cn.kuelcancel.kurrina.management.security;

import java.util.ArrayList;

import cn.kuelcancel.kurrina.management.security.impl.DemoSecurity;
import cn.kuelcancel.kurrina.management.security.impl.ExplosionSecurity;
import cn.kuelcancel.kurrina.management.security.impl.Log4jSecurity;
import cn.kuelcancel.kurrina.management.security.impl.ParticleSecurity;
import cn.kuelcancel.kurrina.management.security.impl.ResourcePackSecurity;
import cn.kuelcancel.kurrina.management.security.impl.TeleportSecurity;

public class SecurityFeatureManager {

	private ArrayList<SecurityFeature> features = new ArrayList<SecurityFeature>();
	
	public SecurityFeatureManager() {
		features.add(new DemoSecurity());
		features.add(new ExplosionSecurity());
		features.add(new Log4jSecurity());
		features.add(new ParticleSecurity());
		features.add(new ResourcePackSecurity());
		features.add(new TeleportSecurity());
	}

	public ArrayList<SecurityFeature> getFeatures() {
		return features;
	}
}
