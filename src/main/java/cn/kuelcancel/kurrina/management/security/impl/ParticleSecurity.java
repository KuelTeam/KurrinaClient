package cn.kuelcancel.kurrina.management.security.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.security.SecurityFeature;
import net.minecraft.network.play.server.S2APacketParticles;

public class ParticleSecurity extends SecurityFeature {

	private int particles;
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S2APacketParticles) {
			S2APacketParticles particle = ((S2APacketParticles) event.getPacket());
			
			particles += particle.getParticleCount();
			particles -= 6;
			particles = Math.min(particles, 150);
			
			if(particles > 100 || particle.getParticleCount() < 1 || Math.abs(particle.getParticleCount()) > 20 || particle.getParticleSpeed() < 0 || particle.getParticleSpeed() > 1000) {
				event.setCancelled(true);
			}
		}
	}
}
