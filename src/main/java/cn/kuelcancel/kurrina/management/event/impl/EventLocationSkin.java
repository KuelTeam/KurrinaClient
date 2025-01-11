package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

public class EventLocationSkin extends Event {
	
	private NetworkPlayerInfo playerInfo;
	private ResourceLocation skin;

	public EventLocationSkin(NetworkPlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
	
	public NetworkPlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public ResourceLocation getSkin() {
		return skin;
	}

	public void setSkin(ResourceLocation skin) {
		this.skin = skin;
	}
}
