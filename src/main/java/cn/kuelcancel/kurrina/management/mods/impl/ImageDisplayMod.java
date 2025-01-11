package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.HUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ImageSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;

public class ImageDisplayMod extends HUDMod {

	private NumberSetting radiusSetting = new NumberSetting(TranslateText.RADIUS, this, 6, 2, 64, true);
	private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 1.0F, 0.0F, 1.0F, false);
	private BooleanSetting shadowSetting = new BooleanSetting(TranslateText.SHADOW, this, false);
	private ImageSetting imageSetting = new ImageSetting(TranslateText.IMAGE, this);
	
	private BufferedImage image;
	private File prevImage;
	
	public ImageDisplayMod() {
		super(TranslateText.IMAGE_DISPLAY, TranslateText.IMAGE_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Kurrina.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		if(imageSetting.getImage() != null && prevImage != imageSetting.getImage()) {
			
			prevImage = imageSetting.getImage();
			
			try {
				image = ImageIO.read(imageSetting.getImage());
			} catch (IOException e) {}
		}
		
		if(image != null) {
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			if(width > 500 || height > 500) {
				
				if((width < 1000 || height < 1000)) {
					width = width / 2;
					height = height / 2;
				}
				
				if((width > 1000 || height > 1000)) {
					width = width / 3;
					height = height / 3;
				}
			}
			
			if(shadowSetting.isToggled()) {
				this.drawShadow(0, 0, width, height, radiusSetting.getValueFloat());
			}
			
			this.drawRoundedImage(imageSetting.getImage(), 0, 0, width, height, radiusSetting.getValueFloat(), alphaSetting.getValueFloat());
			
			this.setWidth(width);
			this.setHeight(height);
		}
	}
}
