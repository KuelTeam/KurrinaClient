package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventWaterOverlay;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;

public class ClearWaterMod extends Mod {
	
	public ClearWaterMod() {
		super(TranslateText.CLEAR_WATER, TranslateText.CLEAR_WATER_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onWaterOverlay(EventWaterOverlay event) {
		event.setCancelled(true);
	}
}
