package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;

public class SlowSwingMod extends Mod {

	private static SlowSwingMod instance;
	
	private NumberSetting delaySetting = new NumberSetting(TranslateText.DELAY, this, 14, 2, 20, true);
	
	public SlowSwingMod() {
		super(TranslateText.SLOW_SWING, TranslateText.SLOW_SWING_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static SlowSwingMod getInstance() {
		return instance;
	}

	public NumberSetting getDelaySetting() {
		return delaySetting;
	}
}
