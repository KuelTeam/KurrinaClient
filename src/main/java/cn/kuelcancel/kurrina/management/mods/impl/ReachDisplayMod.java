package cn.kuelcancel.kurrina.management.mods.impl;

import java.text.DecimalFormat;

import cn.kuelcancel.kurrina.management.event.EventTarget;
import cn.kuelcancel.kurrina.management.event.impl.EventDamageEntity;
import cn.kuelcancel.kurrina.management.event.impl.EventRender2D;
import cn.kuelcancel.kurrina.management.language.TranslateText;
import cn.kuelcancel.kurrina.management.mods.SimpleHUDMod;
import cn.kuelcancel.kurrina.management.mods.settings.impl.BooleanSetting;
import cn.kuelcancel.kurrina.management.nanovg.font.Icon;
import net.minecraft.util.MovingObjectPosition;

public class ReachDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	private DecimalFormat df = new DecimalFormat("0.##");
	
	private double distance = 0;
	private long hitTime =  -1;
	
	public ReachDisplayMod() {
		super(TranslateText.REACH_DISPLAY, TranslateText.REACH_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@EventTarget
	public void onDamageEntity(EventDamageEntity event) {
		if(mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
			hitTime = System.currentTimeMillis();
		}
	}
	
	@Override
	public String getText() {
		
		if((System.currentTimeMillis() - hitTime) > 5000) {
			distance = 0;
		}
		
		if(distance == 0) {
			return "Hasn't attacked";
		}else {
			return df.format(distance) + " blocks";
		}
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.ACTIVITY : null;
	}
}
