package cn.kuelcancel.kurrina.management.mods.impl;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventKey;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.event.impl.EventTick;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.utils.TimerUtils;

public class StopwatchMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_P);
	
	private TimerUtils timer = new TimerUtils();
	private int pressCount;
	private float currentTime;
	private DecimalFormat timeFormat = new DecimalFormat("0.00");
	
	public StopwatchMod() {
		super(TranslateText.STOPWATCH, TranslateText.STOPWATCH_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		switch(pressCount){
			case 0:
				timer.reset();
				break;
			case 1:
				currentTime = (timer.getElapsedTime() / 1000F);
				break;
			case 3:
				timer.reset();
				currentTime = 0;
				pressCount = 0;
				break;
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKeyCode() == keybindSetting.getKeyCode()) {
			pressCount++;
		}
	}
	
	@Override
	public String getText() {
		return timeFormat.format(currentTime) + " s";
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.WATCH : null;
	}
	
	@Override
	public void onEnable() {
		
		super.onEnable();
		
		if(timer != null) {
			timer.reset();
		}
		
		pressCount = 0;
		currentTime = 0;
	}
}
