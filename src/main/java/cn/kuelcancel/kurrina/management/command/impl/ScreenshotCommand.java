package cn.kuelcancel.kurrina.management.command.impl;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.command.Command;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.utils.transferable.FileTransferable;
import net.minecraft.util.ChatComponentText;

public class ScreenshotCommand extends Command {

	public ScreenshotCommand() {
		super("screenshot");
	}

	@Override
	public void onCommand(String message) {
		
		FileManager fileManager = Kurrina.getInstance().getFileManager();
		String[] args = message.split(" ");
		File file = new File(fileManager.getScreenshotDir(), args[1]);
		
		if(args.length < 1) {
			return;
		}
		
		if(args[0].equals("open")) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(args[0].equals("copy")) {
            FileTransferable selection = new FileTransferable(file);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		}
		
		if(args[0].equals("del")) {
			file.delete();
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(args[1] + " has been deleted"));
		}
	}
}
