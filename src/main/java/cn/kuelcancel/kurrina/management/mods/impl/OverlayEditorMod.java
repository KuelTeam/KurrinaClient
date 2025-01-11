package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventFireOverlay;
import cn.kuelcancel.kurrina.management.event.impl.EventRenderPumpkinOverlay;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.Mod;
import cn.kuelcancel.kurrina.management.mods.ModCategory;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;

public class OverlayEditorMod extends Mod {

	private BooleanSetting hidePumpkinSetting = new BooleanSetting(TranslateText.HIDE_PUMPKIN, this, false);
	private BooleanSetting hideFireSetting = new BooleanSetting(TranslateText.HIDE_FIRE, this, false);
	
	public OverlayEditorMod() {
		super(TranslateText.OVERLAY_EDITOR, TranslateText.OVERLAY_EDITOR_DESCRIPTION, ModCategory.RENDER);
	}
	
	@EventTarget
	public void onRenderPumpkinOverlay(EventRenderPumpkinOverlay event) {
		event.setCancelled(hidePumpkinSetting.isToggled());
	}
	
	@EventTarget
	public void onFireOverlay(EventFireOverlay event) {
		event.setCancelled(hideFireSetting.isToggled());
	}
}
