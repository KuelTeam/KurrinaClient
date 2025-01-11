package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class DamageTiltMod extends Mod {

	private static DamageTiltMod instance;
	
	public DamageTiltMod() {
		super(TranslateText.DAMAGE_TILT, TranslateText.DAMAGE_TILT_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static DamageTiltMod getInstance() {
		return instance;
	}
}
