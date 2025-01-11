package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;

public class FPSSpooferMod extends Mod {

	private static FPSSpooferMod instance;
	
	private NumberSetting multiplierSetting = new NumberSetting(TranslateText.MULTIPLIER, this, 2, 1, 30, true);
	
	public FPSSpooferMod() {
		super(TranslateText.FPS_SPOOFER, TranslateText.FPS_SPOOFER_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}

	public static FPSSpooferMod getInstance() {
		return instance;
	}

	public NumberSetting getMultiplierSetting() {
		return multiplierSetting;
	}
}
