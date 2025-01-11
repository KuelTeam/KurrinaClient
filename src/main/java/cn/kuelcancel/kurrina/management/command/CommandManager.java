package cn.kuelcancel.kurrina.management.command;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.command.impl.IRCCommand;
import cn.kuelcancel.kurrina.management.command.impl.ScreenshotCommand;
import cn.kuelcancel.kurrina.management.command.impl.TranslateCommand;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventSendChat;

import java.util.ArrayList;

public class CommandManager {

	private ArrayList<Command> commands = new ArrayList<Command>();

	public CommandManager() {

		commands.add(new ScreenshotCommand());
		commands.add(new TranslateCommand());
		commands.add(new IRCCommand());


		Kurrina.getInstance().getEventManager().register(this);
	}

	@EventTarget
	public void onSendChat(EventSendChat event) {

		if(event.getMessage().startsWith(".kurrina")) {

			event.setCancelled(true);

			String[] args = event.getMessage().split(" ");

			if(args.length > 1) {
				for(Command c : commands) {
					if(args[1].equals(c.getPrefix())) {
						c.onCommand(event.getMessage().replace(".kurrina ", "").replace(args[1] + " ", ""));
					}
				}
			}
		}
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
