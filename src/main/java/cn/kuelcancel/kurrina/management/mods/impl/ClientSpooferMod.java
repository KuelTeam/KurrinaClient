package cn.kuelcancel.kurrina.management.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.ComboSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.combo.Option;

public class ClientSpooferMod extends Mod {

	private static ClientSpooferMod instance;
	
	private ComboSetting typeSetting = new ComboSetting(TranslateText.TYPE, this, TranslateText.VANILLA, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.VANILLA), new Option(TranslateText.FORGE))));
	
	public ClientSpooferMod() {
		super(TranslateText.CLIENT_SPOOFER, TranslateText.CLIENT_SPOOFER_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}

	public static ClientSpooferMod getInstance() {
		return instance;
	}

	public ComboSetting getTypeSetting() {
		return typeSetting;
	}
}
