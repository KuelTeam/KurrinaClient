package cn.kuelcancel.kurrina.gui.modmenu.category.impl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.modmenu.GuiModMenu;
import cn.kuelcancel.kurrina.gui.modmenu.category.Category;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.SettingScene;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.impl.AppearanceScene;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.impl.LanguageScene;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.utils.animation.normal.Animation;
import cn.kuelcancel.kurrina.utils.animation.normal.Direction;
import cn.kuelcancel.kurrina.utils.animation.normal.other.SmoothStepAnimation;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;

public class SettingCategory extends Category {
	
	private Animation sceneAnimation;
	
	private ArrayList<SettingScene> scenes = new ArrayList<SettingScene>();
	private SettingScene currentScene;
	
	public SettingCategory(GuiModMenu parent) {
		super(parent, TranslateText.SETTINGS, Icon.SETTINGS, false);
		
		scenes.add(new AppearanceScene(this));
		scenes.add(new LanguageScene(this));
	}
	
	@Override
	public void initGui() {
		sceneAnimation = new SmoothStepAnimation(260, 1.0);
		sceneAnimation.setValue(1.0);
		
		for(SettingScene scene : scenes) {
			scene.initGui();
		}
	}

	@Override
	public void initCategory() {
		scroll.resetAll();
		sceneAnimation = new SmoothStepAnimation(260, 1.0);
		sceneAnimation.setValue(1.0);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorPalette palette = instance.getColorManager().getPalette();
		
		float offsetY = 15;
		
		if(sceneAnimation.isDone(Direction.FORWARDS)) {
			this.setCanClose(true);
			currentScene = null;
		}
		
		nvg.save();
		nvg.translate((float) -(600 - (sceneAnimation.getValue() * 600)), 0);
		
		for(SettingScene scene : scenes) {
			
			nvg.drawRoundedRect(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40, 8, palette.getBackgroundColor(ColorType.DARK));
			nvg.drawText(scene.getName(), this.getX() + 52, this.getY() + offsetY + 15F, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
			nvg.drawText(scene.getIcon(), this.getX() + 26, this.getY() + offsetY + 14, palette.getFontColor(ColorType.DARK), 14, Fonts.ICON);
			nvg.drawText(scene.getDescription(), this.getX() + (nvg.getTextWidth(scene.getName(), 14, Fonts.MEDIUM)) + 56, this.getY() + offsetY + 17, palette.getFontColor(ColorType.NORMAL), 9, Fonts.REGULAR);
			nvg.drawText(">", this.getX() + this.getWidth() - 32, this.getY() + offsetY + 17, palette.getFontColor(ColorType.NORMAL), 10, Fonts.MEDIUM);
			
			offsetY+=50;
		}
		
		nvg.restore();
		
		nvg.save();
		nvg.translate((float) (sceneAnimation.getValue() * 600), 0);
		
		if(currentScene != null) {
			currentScene.drawScreen(mouseX, mouseY, partialTicks);
		}
		
		nvg.restore();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		float offsetY = 15;
		
		for(SettingScene scene : scenes) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40) && mouseButton == 0 && currentScene == null) {
				currentScene = scene;
				this.setCanClose(false);
				sceneAnimation.setDirection(Direction.BACKWARDS);
			}
			
			offsetY+=50;
		}
		
		if(currentScene != null && sceneAnimation.isDone(Direction.BACKWARDS)) {
			currentScene.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		if(currentScene != null && mouseButton == 3) {
			sceneAnimation.setDirection(Direction.FORWARDS);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		if(currentScene != null && sceneAnimation.isDone(Direction.BACKWARDS)) {
			currentScene.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		if(currentScene != null && keyCode == Keyboard.KEY_ESCAPE) {
			sceneAnimation.setDirection(Direction.FORWARDS);
		}
	}
	
	public int getSceneX() {
		return getX() + 15;
	}
	
	public int getSceneY() {
		return getY() + 15;
	}
	
	public int getSceneWidth() {
		return getWidth() - 30;
	}
	
	public int getSceneHeight() {
		return getHeight() - 30;
	}
}
