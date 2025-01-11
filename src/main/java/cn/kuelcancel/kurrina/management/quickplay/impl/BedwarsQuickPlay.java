package cn.kuelcancel.kurrina.management.quickplay.impl;

import java.util.ArrayList;

import cn.kuelcancel.kurrina.management.quickplay.QuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.QuickPlayCommand;
import net.minecraft.util.ResourceLocation;

public class BedwarsQuickPlay extends QuickPlay {

	public BedwarsQuickPlay() {
		super("Bedwars", new ResourceLocation("kurrina/icons/hypixel/Bedwars.png"));
	}

	@Override
	public void addCommands() {
		
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/l b"));
		commands.add(new QuickPlayCommand("Solo", "/play bedwars_eight_one"));
		commands.add(new QuickPlayCommand("Double", "/play bedwars_eight_two"));
		commands.add(new QuickPlayCommand("3v3v3v3", "/play bedwars_four_three"));
		commands.add(new QuickPlayCommand("4v4v4v4", "/play bedwars_four_four"));
		commands.add(new QuickPlayCommand("4v4", "/play bedwars_two_four"));
		commands.add(new QuickPlayCommand("Castle", "/play bedwars_castle"));
		
		this.setCommands(commands);
	}
}
