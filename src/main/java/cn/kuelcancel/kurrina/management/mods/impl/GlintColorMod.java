package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ColorSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.combo.Option;
import cn.kuelcancel.kurrina.utils.ColorUtils;

public class GlintColorMod extends Mod {

	private static GlintColorMod instance;
	
	private ComboSetting typeSetting = new ComboSetting(TranslateText.TYPE, this, TranslateText.SYNC, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.SYNC), new Option(TranslateText.RAINBOW), new Option(TranslateText.CUSTOM))));
	
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, Color.RED, false);	
	
	public GlintColorMod() {
		super(TranslateText.GLINT_COLOR, TranslateText.GLINT_COLOR_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static GlintColorMod getInstance() {
		return instance;
	}

	public Color getGlintColor() {
		
		Option type = typeSetting.getOption();
		
		if(type.getTranslate().equals(TranslateText.SYNC)) {
			
			AccentColor currentColor = Kurrina.getInstance().getColorManager().getCurrentColor();
			
			return currentColor.getInterpolateColor();
		}else if(type.getTranslate().equals(TranslateText.RAINBOW)) {
			return ColorUtils.getRainbow(0, 25, 255);
		}else if(type.getTranslate().equals(TranslateText.CUSTOM)) {
			return ColorUtils.applyAlpha(colorSetting.getColor(), 255);
		}else {
			return Color.RED;
		}
	}
}
