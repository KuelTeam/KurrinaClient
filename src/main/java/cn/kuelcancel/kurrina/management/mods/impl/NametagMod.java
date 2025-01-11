package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class NametagMod extends Mod {

	private static NametagMod instance;
	
	public NametagMod() {
		super(TranslateText.NAMETAG, TranslateText.NAMETAG_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static NametagMod getInstance() {
		return instance;
	}
}
