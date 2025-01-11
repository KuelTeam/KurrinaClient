package cn.kuelcancel.kurrina.management.mods.impl;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;

public class DayCounterMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public DayCounterMod() {
		super(TranslateText.DAY_COUNTER, TranslateText.DAY_COUNTER_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		long time = mc.theWorld.getWorldInfo().getWorldTotalTime() / 24000L;
		
		return time + " Day" + (time != 1L ? "s" :"");
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.SUNRISE : null;
	}
}
