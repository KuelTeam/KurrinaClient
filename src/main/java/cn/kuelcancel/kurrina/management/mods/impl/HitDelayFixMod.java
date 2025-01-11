package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class HitDelayFixMod extends Mod {

	private static HitDelayFixMod instance;
	
	public HitDelayFixMod() {
		super(TranslateText.HIT_DELAY_FIX, TranslateText.HIT_DELAY_FIX_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static HitDelayFixMod getInstance() {
		return instance;
	}
}
