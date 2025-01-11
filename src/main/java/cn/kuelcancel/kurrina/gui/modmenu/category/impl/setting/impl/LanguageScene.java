package cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.SettingCategory;
import cn.kuelcancel.kurrina.gui.modmenu.category.impl.setting.SettingScene;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.color.palette.ColorPalette;
import cn.kuelcancel.kurrina.management.color.palette.ColorType;
import cn.kuelcancel.kurrina.management.language.Language;
import cn.kuelcancel.kurrina.management.language.LanguageManager;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.nanovg.NanoVGManager;
import cn.kuelcancel.kurrina.management.nanovg.font.Fonts;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.utils.ColorUtils;
import cn.kuelcancel.kurrina.utils.mouse.MouseUtils;

public class LanguageScene extends SettingScene {

	public LanguageScene(SettingCategory parent) {
		super(parent, TranslateText.LANGUAGE, TranslateText.LANGUAGE_DESCRIPTION, Icon.TRANSLATE);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Kurrina instance = Kurrina.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorPalette palette = instance.getColorManager().getPalette();
		AccentColor currentColor = instance.getColorManager().getCurrentColor();
		LanguageManager languageManager = instance.getLanguageManager();
		
		float offsetY = 0;
		
		for(Language lang : Language.values()) {
			
			nvg.drawRoundedRect(this.getX(), this.getY() + offsetY, this.getWidth(), 40, 8, palette.getBackgroundColor(ColorType.DARK));
			nvg.drawRoundedImage(lang.getFlag(), this.getX() + 6, this.getY() + offsetY + 6, 3 * 14, 2 * 14, 4);
			nvg.drawText(lang.getName(), this.getX() + 56, this.getY() + offsetY + 15F, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
			
			lang.getAnimation().setAnimation(lang.equals(languageManager.getCurrentLanguage()) ? 1.0F : 0.0F, 16);
			
			nvg.drawText(Icon.CHECK, this.getX() + this.getWidth() - 28, this.getY() + 12 + offsetY, ColorUtils.applyAlpha(currentColor.getInterpolateColor(), (int) (lang.getAnimation().getValue() * 255)), 16, Fonts.ICON);
			
			offsetY+=50;
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		Kurrina instance = Kurrina.getInstance();
		LanguageManager languageManager = instance.getLanguageManager();
		
		float offsetY = 0;
		
		for(Language lang : Language.values()) {
			
			if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() + offsetY, this.getWidth(), 40) && mouseButton == 0) {
				languageManager.setCurrentLanguage(lang);
			}
			
			offsetY+=50;
		}
	}
}
