package cn.kuelcancel.kurrina.ui.comp.impl;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.file.FileManager;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ImageSetting;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.ui.comp.Comp;
import cn.kuelcancel.kurrina.utils.Multithreading;
import cn.kuelcancel.kurrina.utils.file.FileUtils;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;

public class CompImageSelect extends Comp {

	private ImageSetting imageSetting;
	
	public CompImageSelect(float x, float y, ImageSetting imageSetting) {
		super(x, y);
		this.imageSetting = imageSetting;
	}
	
	public CompImageSelect(ImageSetting imageSetting) {
		super(0, 0);
		this.imageSetting = imageSetting;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		AccentColor accentColor = colorManager.getCurrentColor();
		ColorPalette palette = colorManager.getPalette();
		
		String name = imageSetting.getImage() == null ? TranslateText.NONE.getText() : imageSetting.getImage().getName();
		float nameWidth = nvg.getTextWidth(name, 9, Fonts.REGULAR);
		
		nvg.drawGradientRoundedRect(this.getX(), this.getY(), 16, 16, 4, accentColor.getColor1(), accentColor.getColor2());
		nvg.drawText(name, this.getX() - nameWidth - 5, this.getY() + 4, palette.getFontColor(ColorType.DARK), 9, Fonts.REGULAR);
		nvg.drawCenteredText(Icon.FOLDER, this.getX() + 8, this.getY() + 2.5F, Color.WHITE, 10, Fonts.ICON);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), 16, 16) && mouseButton == 0) {
			
			Multithreading.runAsync(() -> {
				
				File image = FileUtils.selectImageFile();
				
				if(image != null) {
					
					FileManager fileManager = Kurrina.getInstance().getFileManager();
					File cacheDir = new File(fileManager.getCacheDir(), "custom-image");
					
					fileManager.createDir(cacheDir);
					
					File newImage = new File(cacheDir, image.getName());
					
					try {
						FileUtils.copyFile(image, newImage);
					} catch (IOException e) {}
					
					imageSetting.setImage(newImage);
				}
			});
		}
	}
}
