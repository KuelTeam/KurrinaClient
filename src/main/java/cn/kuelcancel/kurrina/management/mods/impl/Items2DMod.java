package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class Items2DMod extends Mod {

	private static Items2DMod instance;
	
	public Items2DMod() {
		super(TranslateText.ITEMS_2D, TranslateText.ITEMS_2D_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		if(ItemPhysicsMod.getInstance().isToggled()) {
			ItemPhysicsMod.getInstance().setToggled(false);
		}
	}
	
	public static Items2DMod getInstance() {
		return instance;
	}
}
