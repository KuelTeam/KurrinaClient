package cn.kuelcancel.kurrina.management.mods.impl;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventClickMouse;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.event.impl.EventTick;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;

public class CPSDisplayMod extends SimpleHUDMod {

	private ArrayList<Long> leftPresses = new ArrayList<Long>();
	private ArrayList<Long> rightPresses = new ArrayList<Long>();
	
	private BooleanSetting rightClickSetting = new BooleanSetting(TranslateText.RIGHT_CLICK, this, true);
	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public CPSDisplayMod() {
		super(TranslateText.CPS_DISPLAY, TranslateText.CPS_DISPLAY_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@EventTarget
	public void onClickMouse(EventClickMouse event) {
		
		if(Mouse.getEventButtonState()) {
			
			if(event.getButton() == 0) {
				leftPresses.add(System.currentTimeMillis());
			}
			
			if(event.getButton() == 1) {
				rightPresses.add(System.currentTimeMillis());
			}
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		leftPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
		rightPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
	}
	
	@Override
	public String getText() {
		return (rightClickSetting.isToggled() ? leftPresses.size() + " | " + rightPresses.size() : leftPresses.size()) + " CPS";
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.MOUSE_POINTER : null;
	}
}
