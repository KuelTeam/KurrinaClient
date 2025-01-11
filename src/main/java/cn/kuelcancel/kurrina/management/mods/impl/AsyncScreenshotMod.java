package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;

public class AsyncScreenshotMod extends Mod {

	private static AsyncScreenshotMod instance;
	
	private BooleanSetting messageSetting = new BooleanSetting(TranslateText.MESSAGE, this, true);
	private BooleanSetting clipboardSetting = new BooleanSetting(TranslateText.CLIPBOARD, this, false);
	
	public AsyncScreenshotMod() {
		super(TranslateText.ASYNC_SCREENSHOT, TranslateText.ASYNC_SCREENSHOT_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}

	public static AsyncScreenshotMod getInstance() {
		return instance;
	}

	public BooleanSetting getMessageSetting() {
		return messageSetting;
	}

	public BooleanSetting getClipboardSetting() {
		return clipboardSetting;
	}
}
