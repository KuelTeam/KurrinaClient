package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;

public class TimeChangerMod extends Mod {

	private static TimeChangerMod instance;
	
	private NumberSetting timeSetting = new NumberSetting(TranslateText.TIME, this, 12, 0, 24, false);
	
	public TimeChangerMod() {
		super(TranslateText.TIME_CHANGER, TranslateText.TIME_CHANGER_DESCRIPTION, ModCategory.WORLD);
		
		instance = this;
	}

	public static TimeChangerMod getInstance() {
		return instance;
	}

	public NumberSetting getTimeSetting() {
		return timeSetting;
	}

}
