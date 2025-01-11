package cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.impl;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.SettingCategory;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.SettingScene;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.ColorManager;
import cn.kuelcancel.kurrina.management.color.Theme;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.impl.GlobalSettingsMod;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.ui.comp.impl.CompComboBox;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;
import cn.kuelcancel.kurrina.utils.mouse.Scroll;

public class AppearanceScene extends SettingScene {

	private Scroll scroll = new Scroll();
	private CompComboBox modTheme;
	
	public AppearanceScene(SettingCategory parent) {
		super(parent, TranslateText.APPEARANCE, TranslateText.APPEARANCE_DESCRIPTION, Icon.MONITOR);
	}

	@Override
	public void initGui() {
		modTheme = new CompComboBox(75, GlobalSettingsMod.getInstance().getModThemeSetting());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		ColorPalette palette = colorManager.getPalette();
		AccentColor currentColor = colorManager.getCurrentColor();
		
		float offsetX = 0;
		int index = 1;
		
		nvg.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), 76, 6, palette.getBackgroundColor(ColorType.DARK));
		nvg.drawText(TranslateText.THEME.getText(), this.getX() + 8, this.getY() + 8, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
		
		for(Theme t : Theme.values()) {
			
			int alpha = (int) (t.getAnimation().getValue() * 255);
			
			nvg.drawRoundedRect(this.getX() + offsetX + 12, this.getY() + 28, 36, 36, 6, t.getNormalBackgroundColor(255));
			
			t.getAnimation().setAnimation(t.equals(colorManager.getTheme()) ? 1.0F : 0.0F, 16);
			
			nvg.drawGradientOutlineRoundedRect(this.getX() + offsetX + 12, this.getY() + 28, 36, 36, 6, 1.4F * t.getAnimation().getValue(), ColorUtils.applyAlpha(currentColor.getColor1(), alpha), ColorUtils.applyAlpha(currentColor.getColor2(), alpha));
			
			offsetX+=46;
		}
		
		offsetX = 0;
		
		nvg.drawRoundedRect(this.getX(), this.getY() + 91, this.getWidth(), 72, 6, palette.getBackgroundColor(ColorType.DARK));
		nvg.drawText(TranslateText.ACCENT_COLOR.getText(), this.getX() + 8, this.getY() + 99, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
		
		nvg.save();
		nvg.scissor(this.getX(), this.getY() + 91, this.getWidth(), 72);
		nvg.translate(scroll.getValue(), 0);
		
		if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() + 91, this.getWidth(), 72)) {
			scroll.onScroll();
			scroll.onAnimation();
		}
		
		for(AccentColor c : colorManager.getColors()) {
			
			nvg.drawGradientRoundedRect(this.getX() + offsetX + 12, this.getY() + 28 + 91, 32, 32, 6, c.getColor1(), c.getColor2());
			
			c.getAnimation().setAnimation(c.equals(currentColor) ? 1.0F : 0.0F, 16);
			
			nvg.drawCenteredText(Icon.CHECK, this.getX() + offsetX + 12 + (32 / 2), this.getY() + 28 + 99, new Color(255, 255, 255, (int) (c.getAnimation().getValue() * 255)), 16, Fonts.ICON);
			
			offsetX+=40F;
			index++;
		}
		
		scroll.setMaxScroll((index - 10.3F) * 40);
		
		nvg.restore();
		
		nvg.drawRoundedRect(this.getX(), this.getY() + 91 + 87, this.getWidth(), 41, 6, palette.getBackgroundColor(ColorType.DARK));
		nvg.drawText(TranslateText.HUD_THEME.getText(), this.getX() + 8, this.getY() + 11.5F + (91 * 2), palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
		
		modTheme.setX(this.getX() + this.getWidth() - 87);
		modTheme.setY(this.getY() + 9.5F + (91 * 2));
		modTheme.draw(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		Kurrina instance = Kurrina.getInstance();
		ColorManager colorManager = instance.getColorManager();
		
		float offsetX = 0;
		
		for(Theme t : Theme.values()) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + offsetX + 12, this.getY() + 28, 36, 36) && mouseButton == 0) {
				colorManager.setTheme(t);
			}
			
			offsetX+=46;
		}
		
		offsetX = scroll.getValue();
		
		for(AccentColor c : colorManager.getColors()) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX() + offsetX + 12, this.getY() + 28 + 91, 32, 32) && mouseButton == 0) {
				colorManager.setCurrentColor(c);
			}
			
			offsetX+=40F;
		}
		
		modTheme.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
