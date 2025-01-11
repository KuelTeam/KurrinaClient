package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.utils.PlayerUtils;

public class GameModeDisplayMod extends SimpleHUDMod {

	public GameModeDisplayMod() {
		super(TranslateText.GAME_MODE_DISPLAY, TranslateText.GAME_MODE_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		String prefix = "Mode: ";
		
		if(PlayerUtils.isSurvival()) {
			return prefix + "Survival";
		}
		
		if(PlayerUtils.isCreative()) {
			return prefix + "Creative";
		}
		
		if(PlayerUtils.isSpectator()) {
			return prefix + "Spectator";
		}
		
		return prefix + "Error";
	}
}
