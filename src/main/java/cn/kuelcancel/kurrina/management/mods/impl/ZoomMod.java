package cn.kuelcancel.kurrina.management.mods.impl;

import org.lwjgl.input.Keyboard;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventZoomFov;
import cn.kuelcancel.kurrina.management.event.impl.EventScrollMouse;
import cn.kuelcancel.kurrina.management.event.impl.EventTick;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.KeybindSetting;
import cn.kuelcancel.kurrina.management.mods.settings.impl.NumberSetting;
import cn.kuelcancel.kurrina.utils.animation.simple.SimpleAnimation;

public class ZoomMod extends Mod {

	private SimpleAnimation zoomAnimation = new SimpleAnimation();
	
	private boolean active;
	private float lastSensitivity;
	private float currentFactor = 1;
	
	public boolean wasCinematic;
	
	private BooleanSetting scrollSetting = new BooleanSetting(TranslateText.SCROLL, this, false);
	private BooleanSetting smoothZoomSetting = new BooleanSetting(TranslateText.SMOOTH_ZOOM, this, false);
	
	private NumberSetting zoomSpeedSetting = new NumberSetting(TranslateText.ZOOM_SPEED, this, 14, 5, 20, true);
	private NumberSetting factorSetting = new NumberSetting(TranslateText.ZOOM_FACTOR, this, 4, 2, 15, true);
	
	private BooleanSetting smoothCameraSetting = new BooleanSetting(TranslateText.SMOOTH_CAMERA, this, true);
	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_C);
	
	public ZoomMod() {
		super(TranslateText.ZOOM, TranslateText.ZOOM_DESCRIPTION, ModCategory.PLAYER);
	}

	@EventTarget
	public void onTick(EventTick event) {
		if(keybindSetting.isKeyDown()) {
			if(!active) {
				active = true;
				lastSensitivity = mc.gameSettings.mouseSensitivity;
				resetFactor();
				wasCinematic = this.mc.gameSettings.smoothCamera;
				mc.gameSettings.smoothCamera = smoothCameraSetting.isToggled();
				mc.renderGlobal.setDisplayListEntitiesDirty();
			}
		}else if(active) {
			active = false;
			setFactor(1);
			mc.gameSettings.mouseSensitivity = lastSensitivity;
			mc.gameSettings.smoothCamera = wasCinematic;
		}
	}
	
	@EventTarget
	public void onFov(EventZoomFov event) {
		
		zoomAnimation.setAnimation(currentFactor, zoomSpeedSetting.getValueFloat());

		event.setFov(event.getFov() * (smoothZoomSetting.isToggled() ? zoomAnimation.getValue() : currentFactor));
	}
	
	@EventTarget
	public void onScroll(EventScrollMouse event) {
		if(active && scrollSetting.isToggled()) {
			event.setCancelled(true);
			if(event.getAmount() < 0) {
				if(currentFactor < 0.98) {
					currentFactor+=0.03;
				}
			}else if(event.getAmount() > 0) {
				if(currentFactor > 0.06) {
					currentFactor-=0.03;
				}
			}
		}
	}
	
	public void resetFactor() {
		setFactor(1 / factorSetting.getValueFloat());
	}

	public void setFactor(float factor) {
		if(factor != currentFactor) {
			mc.renderGlobal.setDisplayListEntitiesDirty();
		}
		currentFactor = factor;
	}
}
