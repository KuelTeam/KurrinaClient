package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class ShinyPotsMod extends Mod {

	private static ShinyPotsMod instance;
	
	public ShinyPotsMod() {
		super(TranslateText.SHINY_POTS, TranslateText.SHINY_POTS_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static ShinyPotsMod getInstance() {
		return instance;
	}
}
