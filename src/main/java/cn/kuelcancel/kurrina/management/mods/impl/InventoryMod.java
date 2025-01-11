package cn.kuelcancel.kurrina.management.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.combo.Option;

public class InventoryMod extends Mod {

	private static InventoryMod instance;

	private BooleanSetting animationSetting = new BooleanSetting(TranslateText.ANIMATION, this, false);
	private ComboSetting animationTypeSetting = new ComboSetting(TranslateText.ANIMATION_TYPE, this, TranslateText.NORMAL, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.NORMAL), new Option(TranslateText.BACKIN))));
	private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
	private BooleanSetting preventPotionShiftSetting = new BooleanSetting(TranslateText.PREVENT_POTION_SHIFT, this, true);
	private BooleanSetting particleSetting = new BooleanSetting(TranslateText.PARTICLE, this, false);
	
	public InventoryMod() {
		super(TranslateText.INVENTORY, TranslateText.INVENTORY_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}

	public static InventoryMod getInstance() {
		return instance;
	}

	public BooleanSetting getAnimationSetting() {
		return animationSetting;
	}

	public ComboSetting getAnimationTypeSetting() {
		return animationTypeSetting;
	}

	public BooleanSetting getBackgroundSetting() {
		return backgroundSetting;
	}

	public BooleanSetting getPreventPotionShiftSetting() {
		return preventPotionShiftSetting;
	}

	public BooleanSetting getParticleSetting() {
		return particleSetting;
	}
}
