package cn.kuelcancel.kurrina;

import java.io.File;

import cn.kuelcancel.kurrina.gui.mainmenu.GuiKurrinaMainMenu;
import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.management.file.FileManager;

public class KurrinaAPI {

	private long launchTime;
	private GuiModMenu modMenu;
	private GuiKurrinaMainMenu mainMenu;
	private File firstLoginFile;
	
	public KurrinaAPI() {
		
		FileManager fileManager = Kurrina.getInstance().getFileManager();
		
		firstLoginFile = new File(fileManager.getCacheDir(), "first.tmp");
	}
	
	public void init() {
		launchTime = System.currentTimeMillis();
		modMenu = new GuiModMenu();
		mainMenu = new GuiKurrinaMainMenu();
	}
	
	public boolean isSpecialUser() {
		return true;
	}
	
	public GuiModMenu getModMenu() {
		return modMenu;
	}

	public long getLaunchTime() {
		return launchTime;
	}

	public GuiKurrinaMainMenu getMainMenu() {
		return mainMenu;
	}

	public void createFirstLoginFile() {
		Kurrina.getInstance().getFileManager().createFile(firstLoginFile);
	}
	
	public boolean isFirstLogin() {
		return !firstLoginFile.exists();
	}
}
