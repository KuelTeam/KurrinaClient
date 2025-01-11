package cn.kuelcancel.kurrina.management.quickplay.impl;

import java.util.ArrayList;

import cn.kuelcancel.kurrina.management.quickplay.QuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.QuickPlayCommand;
import net.minecraft.util.ResourceLocation;

public class MainLobbyQuickPlay extends QuickPlay{

	public MainLobbyQuickPlay() {
		super("MainLobby", new ResourceLocation("kurrina/icons/hypixel/MainLobby.png"));
	}

	@Override
	public void addCommands(){
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/lobby main"));
		
		this.setCommands(commands);
	}
}