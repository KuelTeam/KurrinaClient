package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;

public class TabEditorMod extends Mod {

	private static TabEditorMod instance;
	
	private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private BooleanSetting headSetting = new BooleanSetting(TranslateText.HEAD, this, true);
    
	public TabEditorMod() {
		super(TranslateText.TAB_EDITOR, TranslateText.TAB_EDITOR_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static TabEditorMod getInstance() {
		return instance;
	}

	public BooleanSetting getBackgroundSetting() {
		return backgroundSetting;
	}

	public BooleanSetting getHeadSetting() {
		return headSetting;
	}
}
