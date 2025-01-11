package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import cn.kuelcancel.kurrina.utils.PlayerUtils;
import net.minecraft.potion.Potion;

public class PotionCounterMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public PotionCounterMod() {
		super(TranslateText.POTION_COUNTER, TranslateText.POTION_COUNTER_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		int amount = PlayerUtils.getPotionsFromInventory(Potion.heal);
		
		return amount + " " + (amount <= 1 ? "pot" : "pots");
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.ARCHIVE : null;
	}
}
