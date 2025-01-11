package cn.kuelcancel.kurrina.management.mods.settings.impl;

import java.io.File;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.settings.Setting;

public class SoundSetting extends Setting {

	private File sound;
	
	public SoundSetting(TranslateText nameTranslate, Mod parent) {
		super(nameTranslate, parent);
		
		this.sound = null;
		
		Kurrina.getInstance().getModManager().addSettings(this);
	}

	@Override
	public void reset() {
		this.sound = null;
	}

	public File getSound() {
		return sound;
	}

	public void setSound(File sound) {
		this.sound = sound;
	}
}
