package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.discord.DiscordRPC;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class DiscordRPCMod extends Mod {

	private DiscordRPC discord = new DiscordRPC();
	
	public DiscordRPCMod() {
		super(TranslateText.DISCORD_RPC, TranslateText.DISCORD_RPC_DESCRIPTION, ModCategory.OTHER);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		discord.start();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if(discord.isStarted()) {
			discord.stop();
		}
	}
}
