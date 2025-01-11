package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventText;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.TextSetting;

public class NameProtectMod extends Mod {

	private TextSetting nameSetting = new TextSetting(TranslateText.NAME, this, "You");
	
	public NameProtectMod() {
		super(TranslateText.NAME_PROTECT, TranslateText.NAME_PROTECT_DESCRIPTION, ModCategory.PLAYER);
	}
	
	@EventTarget
	public void onText(EventText event) {
		event.replace(mc.getSession().getUsername(), nameSetting.getText());
	}
}
