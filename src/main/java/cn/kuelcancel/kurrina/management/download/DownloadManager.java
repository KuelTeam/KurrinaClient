package cn.kuelcancel.kurrina.management.download;

import java.io.File;
import java.util.ArrayList;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.download.file.DownloadFile;
import cn.kuelcancel.kurrina.management.download.file.DownloadZipFile;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.utils.Multithreading;
import cn.kuelcancel.kurrina.utils.file.FileUtils;
import cn.kuelcancel.kurrina.utils.network.HttpUtils;

public class DownloadManager {

	private ArrayList<DownloadFile> downloadFiles = new ArrayList<DownloadFile>();
	
	private boolean downloaded;
	
	public DownloadManager() {
		
		FileManager fileManager = Kurrina.getInstance().getFileManager();
		
		downloaded = false;
		
		downloadFiles.add(new DownloadFile("http://d1.getmc.cn:35160/d/KuelTeam/KurrinaClientData/ytdlp.exe?sign=2lvRPSv6-lKWmgtQ2ZClWn8GXf7qH2v1VMZLsubOykk=:0",
				"ytdlp.exe", new File(fileManager.getExternalDir(), "ytdlp"), 13947126));
		downloadFiles.add(new DownloadZipFile("http://d1.getmc.cn:35160/d/KuelTeam/KurrinaClientData/ffmpeg.zip?sign=PbGanYptWl7gWR7QDLycNmhImBGOZE5cB74tLRM2_Ww=:0",
				"ffmpeg.zip", new File(fileManager.getExternalDir(), "ffmpeg"), 51986763, 147600195));
		downloadFiles.add(new DownloadZipFile("http://d1.getmc.cn:35160/p/KuelTeam/KurrinaClientData/windows.zip?sign=pMvmucQuKjr6F0VGI-TxgN2C9m6brecWiJcLdnWi97w=:0",
				"cef.zip", new File(fileManager.getExternalDir(), "cef"), 115822583, 265676507));
		
		Multithreading.runAsync(() -> startDownloads());
	}
	
	private void startDownloads() {
		
		for(DownloadFile df : downloadFiles) {
			
			if(!df.getOutputDir().exists()) {
				df.getOutputDir().mkdirs();
			}
			
			if(df instanceof DownloadZipFile) {
				
				DownloadZipFile dzf = (DownloadZipFile) df;
				
				if(FileUtils.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {
					
					File outputFile = new File(dzf.getOutputDir(), dzf.getFileName());
					
					HttpUtils.downloadFile(dzf.getUrl(), outputFile);
					FileUtils.unzip(outputFile, dzf.getOutputDir());
					outputFile.delete();
				}
			} else {
				
				File outputFile = new File(df.getOutputDir(), df.getFileName());
				
				if(outputFile.length() != df.getSize()) {
					HttpUtils.downloadFile(df.getUrl(), outputFile);
				}
			}
		}
		
		checkFiles();
	}
	
	private void checkFiles() {
		
		for(DownloadFile df : downloadFiles) {
			
			if(df instanceof DownloadZipFile) {
				
				DownloadZipFile dzf = (DownloadZipFile) df;
				
				if(FileUtils.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {
					startDownloads();
				}
			} else {
				
				File outputFile = new File(df.getOutputDir(), df.getFileName());
				
				if(outputFile.length() != df.getSize()) {
					startDownloads();
				}
			}
		}
		
		downloaded = true;
	}

	public boolean isDownloaded() {
		return downloaded;
	}
}