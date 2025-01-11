package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;

public class UHCOverlayMod extends Mod {

	private static UHCOverlayMod instance;
	
	private NumberSetting goldIngotScaleSetting = new NumberSetting(TranslateText.GOLD_INGOT_SCALE, this, 1.5F, 1.0F, 5.0F, false);
	private NumberSetting goldNuggetScaleSetting = new NumberSetting(TranslateText.GOLD_NUGGET_SCALE, this, 1.5F, 1.0F, 5.0F, false);
	private NumberSetting goldOreScaleSetting = new NumberSetting(TranslateText.GOLD_ORE_SCALE, this, 1.5F, 1.0F, 5.0F, false);
	private NumberSetting goldAppleScaleSetting = new NumberSetting(TranslateText.GOLD_APPLE_SCALE, this, 1.5F, 1.0F, 5.0F, false);
	private NumberSetting skullScaleSetting = new NumberSetting(TranslateText.SKULL_SCALE, this, 1.5F, 1.0F, 5.0F, false);
	
	public UHCOverlayMod() {
		super(TranslateText.UHC_OVERLAY, TranslateText.UHC_OVERLAY_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static UHCOverlayMod getInstance() {
		return instance;
	}

	public NumberSetting getGoldIngotScaleSetting() {
		return goldIngotScaleSetting;
	}

	public NumberSetting getGoldNuggetScaleSetting() {
		return goldNuggetScaleSetting;
	}

	public NumberSetting getGoldOreScaleSetting() {
		return goldOreScaleSetting;
	}

	public NumberSetting getGoldAppleScaleSetting() {
		return goldAppleScaleSetting;
	}

	public NumberSetting getSkullScaleSetting() {
		return skullScaleSetting;
	}
}
