package cn.kuelcancel.kurrina.management.file;

import java.io.File;
import java.io.IOException;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import net.minecraft.client.Minecraft;

public class FileManager {

	private File KurrinaDir, profileDir, cacheDir, musicDir, externalDir,
					screenshotDir;
	
	public FileManager() {
		
		KurrinaDir = new File(Minecraft.getMinecraft().mcDataDir, "kurrina");
		profileDir = new File(KurrinaDir, "profile");
		cacheDir = new File(KurrinaDir, "cache");
		musicDir = new File(KurrinaDir, "music");
		externalDir = new File(KurrinaDir, "external");
		screenshotDir = new File(KurrinaDir, "screenshots");
		
		createDir(KurrinaDir);
		createDir(profileDir);
		createDir(cacheDir);
		createDir(musicDir);
		createDir(externalDir);
		createDir(screenshotDir);
		
		createVersionFile();
	}
	
	private void createVersionFile() {
		
		File versionDir = new File(cacheDir, "version");
		
		createDir(versionDir);
		createFile(new File(versionDir, Kurrina.getInstance().getVersion() + ".tmp"));
	}
	
	public void createDir(File file) {
		file.mkdir();
	}
	
	public void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			KurrinaLogger.error("Failed to create file " + file.getName(), e);
		}
	}

	public File getScreenshotDir() {
		return screenshotDir;
	}

	public File getKurrinaDir() {
		return KurrinaDir;
	}

	public File getProfileDir() {
		return profileDir;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public File getMusicDir() {
		return musicDir;
	}

	public File getExternalDir() {
		return externalDir;
	}
}
