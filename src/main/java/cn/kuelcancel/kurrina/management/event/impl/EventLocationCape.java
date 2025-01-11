package cn.kuelcancel.kurrina.management.event.impl;

import cn.kuelcancel.kurrina.management.event.Event;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

public class EventLocationCape extends Event {
	
	private NetworkPlayerInfo playerInfo;
	private ResourceLocation cape;

	public EventLocationCape(NetworkPlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
	
	public NetworkPlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public ResourceLocation getCape() {
		return cape;
	}

	public void setCape(ResourceLocation cape) {
		this.cape = cape;
	}
}