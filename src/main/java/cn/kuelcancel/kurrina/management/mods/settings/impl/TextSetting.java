package cn.kuelcancel.kurrina.management.mods.settings.impl;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.settings.Setting;

public class TextSetting extends Setting {

	private String defaultText, text;
	
	public TextSetting(TranslateText tText, Mod parent, String text) {
		super(tText, parent);
		this.text = text;
		this.defaultText = text;
		
		Kurrina.getInstance().getModManager().addSettings(this);
	}
	
	@Override
	public void reset() {
		this.text = defaultText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDefaultText() {
		return defaultText;
	}
}
