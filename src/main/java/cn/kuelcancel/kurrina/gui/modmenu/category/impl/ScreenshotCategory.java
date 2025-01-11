package cn.kuelcancel.kurrina.gui.modmenu.category.impl;

import java.awt.Desktop;
import java.io.IOException;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.gui.modmenu.category.Category;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.management.screenshot.Screenshot;
import cn.kuelcancel.kurrina.management.screenshot.ScreenshotManager;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;

public class ScreenshotCategory extends Category {

	private Screenshot currentScreenshot;
	
	private SimpleAnimation leftAnimation = new SimpleAnimation();
	private SimpleAnimation rightAnimation = new SimpleAnimation();
	private SimpleAnimation trashAnimation = new SimpleAnimation();
	
	public ScreenshotCategory(GuiModMenu parent) {
		super(parent, TranslateText.SCREENSHOT, Icon.CAMERA, false);
	}
	
	@Override
	public void initCategory() {
		scroll.resetAll();
	}
	
	@Override
	public void initGui() {
		
		ScreenshotManager screenshotManager = Kurrina.getInstance().getScreenshotManager();
		
		if(currentScreenshot == null && !screenshotManager.getScreenshots().isEmpty()) {
			currentScreenshot = screenshotManager.getScreenshots().get(0);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ScreenshotManager screenshotManager = instance.getScreenshotManager();
		ColorManager colorManager = instance.getColorManager();
		ColorPalette palette = colorManager.getPalette();
		AccentColor accentColor = colorManager.getCurrentColor();
		
		int addX = 42;
		int addY = 12;
		int offsetX = 0;
		int index = 1;
		
		screenshotManager.loadScreenshots();
		
		if(currentScreenshot == null && !screenshotManager.getScreenshots().isEmpty()) {
			currentScreenshot = screenshotManager.getScreenshots().get(0);
		}
		
		leftAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), 42, this.getHeight()) ? 1.0F : 0.0F, 16);
		rightAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 42, this.getY(), 42, this.getHeight()) ? 1.0F : 0.0F, 16);
		
		if(currentScreenshot != null) {
			
			trashAnimation.setAnimation(MouseUtils.isInside(mouseX, mouseY, this.getX() + addX, this.getY() + addY, this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38) ? 1.0F : 0.0F, 16);
			
			nvg.drawRoundedImage(currentScreenshot.getImage(), this.getX() + addX, this.getY() + addY, this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38, 6);
			nvg.drawText(Icon.TRASH, this.getX() + this.getWidth() - 59, this.getY() + addY + 6, palette.getMaterialRed((int) (trashAnimation.getValue() * 255)), 12, Fonts.ICON);
			
			addX = 58;
			
			nvg.drawRoundedRect(this.getX() + addX, this.getY() + this.getHeight() - 40, this.getWidth() - (addX * 2), 30, 6, palette.getBackgroundColor(ColorType.DARK));
			
			nvg.save();
			nvg.translate(scroll.getValue(), 0);
			
			for(Screenshot s : screenshotManager.getScreenshots()) {
				
				int alpha = (int) (s.getSelectAnimation().getValue() * 255);
				
				if(offsetX + scroll.getValue() + 30 > 0 && offsetX + scroll.getValue() < this.getWidth() - 100) {
					
					nvg.drawRoundedRect(this.getX() + offsetX + 62, this.getY() + this.getHeight() - 36, 23, 23, 5, palette.getBackgroundColor(ColorType.NORMAL));
					
					nvg.save();
					nvg.scale(this.getX() + offsetX + 62, this.getY() + this.getHeight() - 31, 0.07F);
					nvg.drawImage(s.getImage(), this.getX() + offsetX + 62, this.getY() + this.getHeight() - 31, 16 * 20, 9 * 20);
					nvg.restore();
					
					s.getSelectAnimation().setAnimation(currentScreenshot.equals(s) ? 1.0F : 0.0F, 16);
					
					nvg.drawGradientOutlineRoundedRect(this.getX() + offsetX + 62, this.getY() + this.getHeight() - 36, 23, 23, 5, s.getSelectAnimation().getValue() * 1.2F, ColorUtils.applyAlpha(accentColor.getColor1(), alpha), ColorUtils.applyAlpha(accentColor.getColor2(), alpha));
				}
				
				offsetX+=27;
				index++;
			}
			

			nvg.restore();
			
			nvg.drawRect(this.getX(), this.getY() + this.getHeight() - 40, addX, 30, palette.getBackgroundColor(ColorType.NORMAL));
			nvg.drawRect(this.getX() + this.getWidth() - addX, this.getY() + this.getHeight() - 40, addX - 14, 30, palette.getBackgroundColor(ColorType.NORMAL));
			
			float leftValue = leftAnimation.getValue();
			float rightValue = rightAnimation.getValue();
			
			nvg.save();
			nvg.translate(10 - (leftValue * 10), 0);
			
			nvg.drawRoundedRect(this.getX() + 20, this.getY() + (this.getHeight() / 2) - 30.5F, 12, 24, 4, palette.getBackgroundColor(ColorType.DARK, (int) (leftValue * 255)));
			nvg.drawText("<", this.getX() + 23F, this.getY() + (this.getHeight() / 2) - 22F, palette.getFontColor(ColorType.DARK, (int) (leftValue * 255)), 9, Fonts.DEMIBOLD);
			
			nvg.restore();
			
			nvg.save();
			nvg.translate(-10 + (rightValue * 10), 0);
			
			nvg.drawRoundedRect(this.getX() + this.getWidth() - 32, this.getY() + (this.getHeight() / 2) - 30.5F, 12, 24, 4, palette.getBackgroundColor(ColorType.DARK, (int) (rightValue * 255)));
			nvg.drawText(">", this.getX() + this.getWidth() - 29, this.getY() + (this.getHeight() / 2) - 22F, palette.getFontColor(ColorType.DARK, (int) (rightValue * 255)), 9, Fonts.DEMIBOLD);
			
			nvg.restore();
		}else {
			
			nvg.drawRoundedRect(this.getX() + addX, this.getY() + addY, this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38, 6, palette.getBackgroundColor(ColorType.DARK));
			nvg.drawCenteredText(Icon.CAMERA, this.getX() + addX + ((this.getWidth() - (addX * 2)) / 2), this.getY() + 68, palette.getFontColor(ColorType.NORMAL), 64, Fonts.ICON);
			
			addX = 58;
			
			nvg.drawRoundedRect(this.getX() + addX, this.getY() + this.getHeight() - 40, this.getWidth() - (addX * 2), 30, 6, palette.getBackgroundColor(ColorType.DARK));
		}
		
		scroll.setMaxScroll(index > 12 ? (index - 12) * 27 : 0);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScreenshotManager screenshotManager = Kurrina.getInstance().getScreenshotManager();
		
		int offsetX = (int) scroll.getValue();
		int addX = 42;
		int addY = 12;
		
		boolean inside = MouseUtils.isInside(mouseX, mouseY, this.getX() + addX, this.getY() + addY, this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38);
		boolean trash = MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 61, this.getY() + addY + 4.5F, 16, 16);
		
		if(trash && mouseButton == 0) {
			
			int index = screenshotManager.getScreenshots().indexOf(currentScreenshot) - 1;
			
			screenshotManager.delete(currentScreenshot);
			
			if(index < 0) {
				index = 0;
			}
			
			if(screenshotManager.getScreenshots().isEmpty()) {
				currentScreenshot = null;
			}else {
				currentScreenshot = screenshotManager.getScreenshots().get(index);
			}
		}
		
		addX = 58;
		
		for(Screenshot s : screenshotManager.getScreenshots()) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + offsetX + 62, this.getY() + this.getHeight() - 36, 23, 23) && mouseButton == 0 &&
					MouseUtils.isInside(mouseX, mouseY, this.getX() + addX, this.getY() + this.getHeight() - 40, this.getWidth() - (addX * 2), 30)) {
				currentScreenshot = s;
			}
			
			offsetX+=27;
		}
		
		if(inside && !trash && mouseButton == 0 && currentScreenshot != null) {
			try {
				Desktop.getDesktop().open(currentScreenshot.getImage());
			} catch (IOException e) {}
		}
		
		if(currentScreenshot != null && mouseButton == 0) {
			

			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 20, this.getY() + (this.getHeight() / 2) - 30.5F, 12, 24)) {
				currentScreenshot = screenshotManager.getBackScreenshot(currentScreenshot);
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 32, this.getY() + (this.getHeight() / 2) - 30.5F, 12, 24)) {
				currentScreenshot = screenshotManager.getNextScreenshot(currentScreenshot);
			}
		}
	}
}
