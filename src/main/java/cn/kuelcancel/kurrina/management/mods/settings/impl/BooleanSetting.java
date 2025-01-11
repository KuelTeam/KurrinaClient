package cn.kuelcancel.kurrina.management.mods.settings.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.settings.Setting;

public class BooleanSetting extends Setting {

	private boolean defaultValue, toggled;
	
	public BooleanSetting(TranslateText text, Mod parent, boolean toggled) {
		super(text, parent);
		
		this.toggled = toggled;
		this.defaultValue = toggled;
		
		Kurrina.getInstance().getModManager().addSettings(this);
	}
	
	@Override
	public void reset() {
		this.toggled = defaultValue;
	}
	
	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggle) {
		this.toggled = toggle;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}
}
