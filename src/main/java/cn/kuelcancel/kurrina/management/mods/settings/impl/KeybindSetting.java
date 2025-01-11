package cn.kuelcancel.kurrina.management.mods.settings.impl;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.Kurrina;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class KeybindSetting extends Setting {

	private int defaultKeyCode, keyCode;
	
	public KeybindSetting(TranslateText text, Mod parent, int keyCode) {
		super(text, parent);
		this.defaultKeyCode = keyCode;
		this.keyCode = keyCode;
		
		Kurrina.getInstance().getModManager().addSettings(this);
	}

	@Override
	public void reset() {
		this.keyCode = defaultKeyCode;
	}
	
	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}
	
	public boolean isKeyDown() {
		return Keyboard.isKeyDown(keyCode) && !(Minecraft.getMinecraft().currentScreen instanceof Gui);
	}
}