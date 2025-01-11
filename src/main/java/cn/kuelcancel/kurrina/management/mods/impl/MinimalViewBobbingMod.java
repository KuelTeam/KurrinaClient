package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class MinimalViewBobbingMod extends Mod {

	private static MinimalViewBobbingMod instance;
	
	public MinimalViewBobbingMod() {
		super(TranslateText.MINIMAL_VIEW_BOBBING, TranslateText.MINIMAL_VIEW_BOBBING_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static MinimalViewBobbingMod getInstance() {
		return instance;
	}
}
