package cn.kuelcancel.kurrina.management.security.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventReceivePacket;
import cn.kuelcancel.kurrina.management.security.SecurityFeature;
import net.minecraft.network.play.server.S27PacketExplosion;

public class ExplosionSecurity extends SecurityFeature {

	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if(event.getPacket() instanceof S27PacketExplosion) {
			
			S27PacketExplosion explosion = ((S27PacketExplosion) event.getPacket());
			
			if(explosion.func_149149_c() >= Byte.MAX_VALUE || explosion.func_149144_d() >= Byte.MAX_VALUE || explosion.func_149149_c() >= Byte.MAX_VALUE) {
				event.setCancelled(true);
			}
		}
	}
}
