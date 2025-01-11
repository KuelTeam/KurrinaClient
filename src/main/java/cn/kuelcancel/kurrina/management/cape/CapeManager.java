package cn.kuelcancel.kurrina.management.cape;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.logger.KurrinaLogger;
import cn.kuelcancel.kurrina.management.cape.impl.Cape;
import cn.kuelcancel.kurrina.management.cape.impl.CustomCape;
import cn.kuelcancel.kurrina.management.cape.impl.NormalCape;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.utils.ImageUtils;
import cn.kuelcancel.kurrina.utils.file.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class CapeManager {

	private Minecraft mc = Minecraft.getMinecraft();
	
	private ArrayList<Cape> capes = new ArrayList<Cape>();
	private Cape currentCape;
	
	public CapeManager() {
		
		Kurrina instance = Kurrina.getInstance();
		FileManager fileManager = instance.getFileManager();
		File customCapeDir = new File(fileManager.getKurrinaDir(), "custom-cape");
		File cacheDir = new File(fileManager.getCacheDir(), "cape");
		
		if(!customCapeDir.exists()) {
			fileManager.createDir(customCapeDir);
		}
		
		if(!cacheDir.exists()) {
			fileManager.createDir(cacheDir);
		}
		
		capes.add(new NormalCape("None", null, null, false, CapeCategory.ALL));
		
		add("Minecon 2011", "minecon/2011-sample.png", "minecon/2011.png", false, CapeCategory.MINECON);
		add("Minecon 2012", "minecon/2012-sample.png", "minecon/2012.png", false, CapeCategory.MINECON);
		add("Minecon 2013", "minecon/2013-sample.png", "minecon/2013.png", false, CapeCategory.MINECON);
		add("Minecon 2015", "minecon/2015-sample.png", "minecon/2015.png", false, CapeCategory.MINECON);
		add("Minecon 2016", "minecon/2016-sample.png", "minecon/2016.png", false, CapeCategory.MINECON);
		add("China", "flag/china-sample.png", "flag/china.png", true, CapeCategory.FLAG);
		add("Canada", "flag/canada-sample.png", "flag/canada.png", true, CapeCategory.FLAG);
		add("France", "flag/france-sample.png", "flag/france.png", true, CapeCategory.FLAG);
		add("Germany", "flag/germany-sample.png", "flag/germany.png", true, CapeCategory.FLAG);
		add("India", "flag/india-sample.png", "flag/india.png", true, CapeCategory.FLAG);
		add("Indonesia", "flag/indonesia-sample.png", "flag/indonesia.png", true, CapeCategory.FLAG);
		add("Italy", "flag/italy-sample.png", "flag/italy.png", true, CapeCategory.FLAG);
		add("Japan", "flag/japan-sample.png", "flag/japan.png", true, CapeCategory.FLAG);
		add("Korean", "flag/korean-sample.png", "flag/korean.png", true, CapeCategory.FLAG);
		add("United Kingdom", "flag/united-kingdom-sample.png", "flag/united-kingdom.png", true, CapeCategory.FLAG);
		add("United States", "flag/united-states-sample.png", "flag/united-states.png", true, CapeCategory.FLAG);
		add("keke", "Special/keke-sample.png", "Special/keke.png", true, CapeCategory.SPECIAL);
		add("Nachoneko", "Special/Nachoneko-sample.png", "Special/Nachoneko.png", true, CapeCategory.SPECIAL);
		add("Kurrina", "Kurrina/Kurrina-sample.png", "Kurrina/Kurrina.png", true, CapeCategory.KURRINA);



		currentCape = getCapeByName("None");

		for(File f : customCapeDir.listFiles()) {
			
			if(FileUtils.isImageFile(f)) {
				
				File file = new File(cacheDir, f.getName() + ".png");
				
				if(!file.exists()) {
					
					try {
						BufferedImage image = ImageIO.read(f);
						int width = image.getWidth();
						int height = image.getHeight();
					
						BufferedImage outputImage = ImageUtils.scissor(image, (int) (width * 0.03125), (int) (height * 0.0625), (int) (width * 0.125), (int) (height * 0.46875));
						
						ImageIO.write(ImageUtils.resize(outputImage, 1000, 1700), "png", file);
					} catch (IOException e) {
						KurrinaLogger.error("Failed to load image", e);
						continue;
					}
				}
				
				if(file.exists()) {
					
					try {
						DynamicTexture cape = new DynamicTexture(ImageIO.read(f));
						
						addCustomCape(f.getName().replace("." + FileUtils.getExtension(f), ""), file,
								mc.getTextureManager().getDynamicTextureLocation(String.valueOf(f.getName().hashCode()), cape), true, CapeCategory.SPECIAL);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		for(Cape c : capes) {
			
			if(c instanceof NormalCape) {
				
				NormalCape cape = (NormalCape) c;
				
				if(cape.getSample() != null) {
					instance.getNanoVGManager().loadImage(cape.getSample());
				}
			}
			
			if(c instanceof CustomCape) {
				
				CustomCape cape = (CustomCape) c;
				
				if(cape.getSample() != null) {
					instance.getNanoVGManager().loadImage(cape.getSample());
				}
			}
			
			if(c.getCape() != null) {
				mc.getTextureManager().bindTexture(c.getCape());
			}
		}
	}
	
	private void add(String name, String samplePath, String capePath, boolean premium, CapeCategory category) {
		
		String cosmeticPath = "kurrina/cosmetics/cape/";
		
		capes.add(new NormalCape(name, new ResourceLocation(cosmeticPath + samplePath), new ResourceLocation(cosmeticPath + capePath), premium, category));
	}
	
	private void addCustomCape(String name, File sample, ResourceLocation cape, boolean premium, CapeCategory category) {
		capes.add(new CustomCape(name, sample, cape, premium, category));
	}
	
	public ArrayList<Cape> getCapes() {
		return capes;
	}
	
	public Cape getCurrentCape() {
		return currentCape;
	}

	public void setCurrentCape(Cape currentCape) {
		this.currentCape = currentCape;
	}

	public Cape getCapeByName(String name) {
		
		for(Cape c : capes) {
			if(c.getName().equals(name)) {
				return c;
			}
		}
		
		return getCapeByName("None");
	}
}
