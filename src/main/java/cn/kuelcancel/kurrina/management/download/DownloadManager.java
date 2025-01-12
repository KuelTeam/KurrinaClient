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
		
		downloadFiles.add(new DownloadFile("https://59-47-225-124.pd1.cjjd19.com:30443/download-cdn.cjjd19.com/123-46/09daede3/1743715-0/09daede3b4bf362f51a04a949d90242e/c-m23?v=5&t=1736745859&s=1736745859cdd90b8ede97f5b851aed93f09b8103f&r=YWWSL5&bzc=2&bzs=313832333637313733343a33323530343038373a31333934373132363a30&filename=ytdlp.exe&x-mf-biz-cid=6ed18cfd-f8e4-4222-acc3-65d62089c07e-a0d664&auto_redirect=0&cache_type=1&xmfcid=d028c663-ef97-4472-8da3-e95317066229-0-abf611255",
				"ytdlp.exe", new File(fileManager.getExternalDir(), "ytdlp"), 13947126));
		downloadFiles.add(new DownloadZipFile("https://59-47-237-138.pd1.cjjd19.com:30443/download-cdn.cjjd19.com/123-8/c2b6ebcf/1823671734-0/c2b6ebcfe6cf49ab747c12fd5b650808/c-m60?v=5&t=1736746828&s=17367468285b485027cb16e5064266f25211ac4393&r=AW70G9&bzc=2&bzs=313832333637313733343a33323530343039313a35313938363736333a30&filename=ffmpeg.zip&x-mf-biz-cid=2ed9cce7-3d58-47ed-94ce-b05b31b79fc3-a0d664&auto_redirect=0&cache_type=1&xmfcid=db9b284b-7d7a-4574-ab35-5ae6a38e3c10-0-9eed82220",
				"ffmpeg.zip", new File(fileManager.getExternalDir(), "ffmpeg"), 51986763, 147600195));
		downloadFiles.add(new DownloadZipFile("https://d.feijix.com/storage/files/2025/01/12/9/5005129499/17366612626651.gz?t=6783604b&rlimit=20&us=nWeMXM18sS&sign=34357366255ed4742c5295b8f2464ddf&download_name=windows.zip",
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