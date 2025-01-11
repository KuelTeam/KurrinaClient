package cn.kuelcancel.kurrina.management.mods.impl;

import java.awt.Color;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.color.AccentColor;
import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventHitOverlay;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ColorSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;

public class HitColorMod extends Mod {

	private BooleanSetting customColorSetting = new BooleanSetting(TranslateText.CUSTOM_COLOR, this, false);
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, new Color(255, 0, 0), false);
    private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 0.45, 0, 1.0, false);
    
	public HitColorMod() {
		super(TranslateText.HIT_COLOR, TranslateText.HIT_COLOR_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onHitOverlay(EventHitOverlay event) {
		
		AccentColor currentColor = Kurrina.getInstance().getColorManager().getCurrentColor();
		Color lastColor = customColorSetting.isToggled() ? colorSetting.getColor() : currentColor.getInterpolateColor();
		
		event.setRed(lastColor.getRed() / 255F);
		event.setGreen(lastColor.getGreen() / 255F);
		event.setBlue(lastColor.getBlue() / 255F);
		event.setAlpha(alphaSetting.getValueFloat());
	}
}
